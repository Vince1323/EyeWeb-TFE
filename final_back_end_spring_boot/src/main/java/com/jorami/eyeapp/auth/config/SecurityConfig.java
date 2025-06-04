package com.jorami.eyeapp.auth.config;
/**
 * Classe de configuration principale de Spring Security.
 * Elle définit les règles d’accès à l’API, les filtres de sécurité, le mode de session (stateless), et la gestion du CORS.
 * C’est dans cette classe qu’on active le filtre JWT et qu’on configure les règles par rôle (ADMIN, USER, etc.).
 */

// --- LOMBOK ---
import lombok.RequiredArgsConstructor;
// Génère automatiquement un constructeur avec tous les champs `final`. Permet l’injection des dépendances dans un style propre.

// --- CONFIGURATION SPRING ---
import org.springframework.context.annotation.Bean;           // Permet de déclarer des beans Spring dans une classe de configuration
import org.springframework.context.annotation.Configuration; // Indique que cette classe contient une configuration Spring Boot

// --- SPRING SECURITY ---
import org.springframework.security.authentication.AuthenticationProvider; // Interface permettant d’implémenter un mécanisme d’authentification personnalisé
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Active la sécurité au niveau des méthodes (ex: @PreAuthorize)
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Permet de construire la configuration HTTP de Spring Security
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Active la sécurité web (filtres, routes protégées)
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // Utilisé ici pour désactiver CSRF
import org.springframework.security.core.context.SecurityContextHolder; // Gère le contexte de sécurité actif (utilisateur connecté)
import org.springframework.security.web.SecurityFilterChain; // Chaîne de filtres de sécurité définie pour toutes les requêtes HTTP
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtre utilisé pour l’authentification classique (on le remplace par JWT)
import org.springframework.security.web.authentication.logout.LogoutHandler; // Permet d’intercepter et personnaliser la logique de logout

// --- CORS ---
import org.springframework.web.cors.CorsConfiguration; // Représente une configuration CORS (Cross-Origin Resource Sharing)
import org.springframework.web.cors.CorsConfigurationSource; // Source pour charger la configuration CORS
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // Implémentation permettant d’associer des URLs à une config CORS

// --- JAVA UTILS ---
import java.util.List; // Utilisé ici pour configurer les listes d’origines, méthodes, et headers autorisés dans le CORS

// --- CONSTANTES ---
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS; // Mode stateless pour les APIs (aucune session)


// === ANNOTATIONS DE CLASSE ===

@Configuration
// Indique à Spring que cette classe contient des beans de configuration à charger au démarrage de l’application

@EnableWebSecurity
// Active les fonctionnalités de sécurité Spring pour les requêtes HTTP

@RequiredArgsConstructor
// Génère un constructeur avec tous les champs `final` (jwtAuthFilter, authenticationProvider, logoutHandler)

@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
// Active les annotations de sécurité sur les méthodes, comme @Secured ou @PreAuthorize

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; // Notre filtre de sécurité personnalisé pour analyser les JWT
    private final AuthenticationProvider authenticationProvider; // Fournisseur d’authentification défini ailleurs (ex: DAO)
    private final LogoutHandler logoutHandler; // Handler personnalisé pour gérer la déconnexion utilisateur

    /**
     * Définit la chaîne de sécurité appliquée à chaque requête HTTP.
     * On y configure : CORS, désactivation CSRF, accès aux routes selon les rôles,
     * session stateless, ajout du filtre JWT, gestion du logout.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http    // CORS : configuration personnalisée pour autoriser le frontend Angular
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // CSRF désactivé car l’application est stateless (pas de sessions serveur)
                // Configuration des autorisations par route
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/auth/**").permitAll(); // Auth public : login, register
                    authorize.requestMatchers("/admin/**").hasRole("ADMIN"); // Accès restreint à ADMIN
                    authorize.requestMatchers("/doctor/**").hasAnyRole("DOCTOR", "ADMIN");
                    authorize.requestMatchers("/secretary/**").hasAnyRole("SECRETARY", "ADMIN");
                    authorize.requestMatchers("/user/**").hasAnyRole("USER", "SECRETARY", "DOCTOR", "ADMIN");
                    authorize.anyRequest().authenticated(); // Toute autre requête nécessite une authentification
                })
                // Le mode stateless évite de stocker l'état utilisateur côté serveur (important pour les JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                // Fournisseur d’authentification utilisé
                .authenticationProvider(authenticationProvider)
                // Ajout du filtre JWT AVANT le filtre standard d'authentification
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // Configuration du logout
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // URL de déconnexion
                        .addLogoutHandler(logoutHandler) // Comportement personnalisé à la déconnexion
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // Nettoyage du contexte de sécurité
                );
        // On retourne la configuration de sécurité complète
        return http.build();
    }

    /**
     * Configuration CORS pour permettre au frontend Angular de communiquer avec le backend.
     * On autorise l'origine localhost:4200 (dev) et le domaine de prod eyewebapp.com.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200", "https://eyewebapp.com")); // Origines autorisées
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées
        configuration.setAllowedHeaders(List.of("*")); // Autoriser tous les headers (ex: Authorization)
        configuration.setAllowCredentials(true); // Autorise l’envoi des cookies / headers sécurisés

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Applique à toutes les routes
        return source;
    }


}
