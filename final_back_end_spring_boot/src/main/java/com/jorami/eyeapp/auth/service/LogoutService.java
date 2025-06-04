package com.jorami.eyeapp.auth.service;

// Permet d’accéder à la requête HTTP entrante et à la réponse HTTP sortante
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Représente l’utilisateur actuellement authentifié dans Spring Security
import org.springframework.security.core.Authentication;

/**
 * Interface représentant le contrat pour gérer la déconnexion (logout) d’un utilisateur.
 * Elle est implémentée par `LogoutServiceImpl`, qui s’occupe d’invalider le token JWT et de nettoyer le contexte de sécurité.
 */
public interface LogoutService {

    /**
     * Méthode appelée lors du logout d’un utilisateur.
     * Elle permet d’invalider le token JWT utilisé et de vider le contexte de sécurité.
     *
     * @param request        la requête HTTP reçue (permet de lire l’en-tête Authorization)
     * @param response       la réponse HTTP (souvent inutilisée ici mais requise par l’interface)
     * @param authentication les informations d’authentification actuelles de l’utilisateur
     */
    void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

}
