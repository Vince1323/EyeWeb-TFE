package com.jorami.eyeapp.auth.service.implementation;

// Repositories Spring Data pour accéder aux tokens JWT persistés
import com.jorami.eyeapp.auth.repository.TokenRepository;

// Interface métier Logout (couche service)
import com.jorami.eyeapp.auth.service.LogoutService;

// Classes servlet pour accéder aux détails de la requête HTTP
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Lombok : génère automatiquement le constructeur avec les dépendances `final`
import lombok.RequiredArgsConstructor;

// Spring Security
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

// Indique que cette classe est un composant de service Spring
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService, LogoutHandler {

    // Injection du repository permettant d’accéder aux tokens
    private final TokenRepository tokenRepository;

    /**
     * Méthode appelée automatiquement par Spring Security lorsqu’un utilisateur se déconnecte.
     * Elle va marquer le token JWT comme révoqué et expiré dans la base.
     * Cela permet d’invalider le token, même s’il est toujours techniquement valide côté JWT.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // On récupère l’en-tête HTTP "Authorization"
        final String authHeader = request.getHeader("Authorization");

        // On vérifie que le header contient bien un token JWT (prefixé par "Bearer ")
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return; // Pas de token fourni → rien à faire
        }

        // Extraction du token : on retire "Bearer " (7 caractères)
        jwt = authHeader.substring(7);

        // On recherche le token en base
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);

        // Si le token existe, on le marque comme expiré et révoqué
        if (storedToken != null) {
            storedToken.setExpired(true); // JWT n’est plus valide côté base
            storedToken.setRevoked(true); // JWT est officiellement désactivé
            tokenRepository.save(storedToken); // On sauvegarde ces changements

            // On vide le contexte de sécurité (l’utilisateur est déconnecté)
            SecurityContextHolder.clearContext();
        }
    }
}
