package com.jorami.eyeapp.auth.service;

// Importation des modèles utilisés pour l’authentification
import com.jorami.eyeapp.auth.model.*;
import com.jorami.eyeapp.model.User;

import java.io.UnsupportedEncodingException;

/**
 * Interface définissant les fonctionnalités principales liées à l’authentification.
 * Cette interface est utilisée pour séparer l’abstraction (définition des comportements) de l’implémentation réelle.
 * Elle est implémentée par la classe AuthenticationServiceImpl.
 */
public interface AuthenticationService {

    /**
     * Enregistre un nouvel utilisateur à partir des informations fournies dans RegistrationRequest.
     * @param request objet contenant les données d’inscription (nom, email, mot de passe, etc.)
     * @throws UnsupportedEncodingException si un problème survient lors de l’envoi d’un e-mail contenant des caractères non supportés
     */
    void register(RegistrationRequest request) throws UnsupportedEncodingException;

    /**
     * Authentifie un utilisateur à partir de son email et mot de passe, et génère un token JWT s’il est valide.
     * @param authenticationRequest contient l’email et le mot de passe
     * @return un objet AuthenticationResponse contenant le token JWT
     */
    AuthenticationResponse authentication(AuthenticationRequest authenticationRequest);

    /**
     * Envoie un email contenant un code de réinitialisation du mot de passe à l'utilisateur.
     * @param user l'utilisateur concerné
     * @throws UnsupportedEncodingException si une erreur d'encodage se produit pendant l'envoi du mail
     */
    void confirmResetPassword(User user) throws UnsupportedEncodingException;

    /**
     * Valide un code de vérification envoyé par email.
     * Cette méthode confirme que l’email de l’utilisateur est bien vérifié.
     * @param verificationCode le code de validation à vérifier
     * @throws UnsupportedEncodingException si le message d’email de confirmation contient des caractères non supportés
     */
    void validateCode(VerificationCode verificationCode) throws UnsupportedEncodingException;

    /**
     * Réinitialise le mot de passe d’un utilisateur après validation du code de vérification.
     * @param userSource l’utilisateur à mettre à jour
     * @param resetPasswordRequest les informations nécessaires : nouveau mot de passe et code de vérification
     * @return l'utilisateur mis à jour avec son nouveau mot de passe
     */
    User resetPassword(User userSource, ResetPasswordRequest resetPasswordRequest);

}
