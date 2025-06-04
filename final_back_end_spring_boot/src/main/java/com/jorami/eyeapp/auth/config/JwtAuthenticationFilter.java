package com.jorami.eyeapp.auth.config;
/**
 * Définit un filtre de sécurité personnalisé pour gérer l’authentification par token JWT.
 * Il s’assure que chaque requête HTTP porte un token valide avant d’autoriser l’accès à des routes protégées.
 * -----------------------------------------------------------------------------------------------------
 * Ce filtre est exécuté automatiquement à chaque requête.
 * Il vérifie si un JWT est présent dans l’en-tête Authorization.
 * Il valide le token, puis authentifie manuellement l’utilisateur.
 * Le contexte Spring Security (SecurityContextHolder) est mis à jour avec les infos de l’utilisateur.
 * Il garantit un modèle stateless (pas de session) : le token suffit à identifier l'utilisateur.
 * -----------------------------------------------------------------------------------------------------
 */

// --- IMPORTS MÉTIER (repositories et services) ---
import com.jorami.eyeapp.auth.repository.TokenRepository; // Accès aux tokens persistés (vérifie s’ils sont expirés ou révoqués)
import com.jorami.eyeapp.auth.service.JwtService; // Gère l’extraction, validation et lecture du token JWT

// --- IMPORTS SERVLETS (traitement des requêtes HTTP) ---
import jakarta.servlet.FilterChain; // Chaîne de filtres HTTP exécutée pour chaque requête
import jakarta.servlet.ServletException; // Exception levée en cas de problème côté servlet
import jakarta.servlet.http.HttpServletRequest; // Représente la requête HTTP
import jakarta.servlet.http.HttpServletResponse; // Représente la réponse HTTP

// --- LOMBOK ---
import lombok.RequiredArgsConstructor;
// Génère automatiquement un constructeur avec tous les champs `final` injectés (injection par constructeur)

// --- SPRING SECURITY ---
import org.springframework.lang.NonNull; // Spécifie que l’argument ne peut pas être null
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Représente un utilisateur authentifié
import org.springframework.security.core.GrantedAuthority; // Représente un rôle (ex : ROLE_USER)
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Implémentation simple d’un rôle
import org.springframework.security.core.context.SecurityContextHolder; // Contient l’utilisateur authentifié pour la requête en cours
import org.springframework.security.core.userdetails.UserDetails; // Interface Spring représentant un utilisateur
import org.springframework.security.core.userdetails.UserDetailsService; // Permet de charger les utilisateurs depuis la base
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Fournit des infos supplémentaires sur la requête (IP, session)

// --- SPRING FRAMEWORK ---
import org.springframework.stereotype.Service;
// Indique que la classe est un composant métier Spring pouvant être injecté comme un bean

import org.springframework.web.filter.OncePerRequestFilter;
// Filtre de sécurité exécuté une seule fois par requête HTTP (contrairement aux filtres classiques)

// --- JAVA UTILITIES ---
import java.io.IOException; // Gère les exceptions d’entrée/sortie
import java.util.ArrayList; // Permet de stocker dynamiquement les rôles dans une liste
import java.util.Collection; // Interface générale de collection
import java.util.List; // Interface de liste Java

// --- CONSTANTES ---
import static com.jorami.eyeapp.exception.ConstantMessage.VALIDATE_TOKEN; // Constante personnalisée pour les messages d’erreur liés aux tokens
import static org.springframework.http.HttpHeaders.AUTHORIZATION; // Constante HTTP standard pour accéder à l’en-tête Authorization

// === ANNOTATIONS ===

@Service
// Déclare la classe comme un composant Spring de type "service" injecté automatiquement dans le contexte de l’application.
// Nécessaire pour qu’elle soit détectée et utilisée dans la configuration de Spring Security.

@RequiredArgsConstructor
// Lombok génère automatiquement un constructeur avec tous les champs marqués `final`.
// Permet l’injection des dépendances (jwtService, userDetailsService, tokenRepository) sans boilerplate.

public class JwtAuthenticationFilter extends OncePerRequestFilter {
// Classe qui hérite de OncePerRequestFilter => garantit que ce filtre ne sera exécuté qu’une seule fois par requête HTTP.
// Ce filtre est déclenché automatiquement pour chaque requête entrante grâce à Spring Security.

    /**
     * Filtres Spring Security personnalisés qui ne s’exécutent qu’une seule fois par requête.
     * Ce filtre intercepte les requêtes HTTP pour gérer l’authentification basée sur un token JWT.
     */
    private final JwtService jwtService; // Service pour manipuler les JWT (lecture, validation, extraction)
    private final UserDetailsService userDetailsService; // Permet de charger les utilisateurs depuis la base
    private final TokenRepository tokenRepository; // Accès aux tokens persistés en base pour vérifier leur validité

    /**
     * Méthode appelée automatiquement à chaque requête HTTP.
     * Elle vérifie la présence et la validité d’un token JWT dans l’en-tête Authorization,
     * puis elle authentifie l'utilisateur si tout est correct.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // On ignore les endpoints d’authentification (comme /api/auth/register ou /api/auth/login)
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        // On récupère l’en-tête Authorization (format attendu : "Bearer eyJhbGciOiJIUzI1NiIsInR...")
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String userEmail;

        // Si aucun token ou mauvais format (ne commence pas par "Bearer "), on passe au filtre suivant
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // On extrait le token JWT en supprimant le préfixe "Bearer "
        jwt = authHeader.substring(7);

        // On extrait l’email (ou username) de l’utilisateur à partir du token
        userEmail = jwtService.extractUsername(jwt);

        // Si le token contient bien un email et que l'utilisateur n'est pas encore authentifié pour cette requête
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // On récupère les informations de l'utilisateur depuis la base
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            // On vérifie en base que ce token n’est ni expiré ni révoqué
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            // Si le token est bien valide (signature, expiration, utilisateur, et en base)
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                // On récupère les autorités (rôles) de l'utilisateur depuis UserDetails
                Collection<? extends GrantedAuthority> existingAuthorities = userDetails.getAuthorities();

                // On extrait l’autorité du token JWT (ex: ROLE_ADMIN)
                SimpleGrantedAuthority jwtAuthority = jwtService.getAuthoritie(jwt);

                // On combine les rôles provenant de la base et du token
                List<GrantedAuthority> combinedAuthorities = new ArrayList<>(existingAuthorities);
                combinedAuthorities.add(jwtAuthority);

                // On crée le token d’authentification pour Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        combinedAuthorities
                );

                // On ajoute les détails de la requête (ex: adresse IP, session) au token
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // On enregistre l’utilisateur dans le contexte de sécurité de Spring
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // Si le token est invalide malgré tout, on lève une exception
                throw new RuntimeException(VALIDATE_TOKEN);
            }
        }

        // On laisse passer la requête vers le contrôleur ou les filtres suivants
        filterChain.doFilter(request, response);
    }
}
