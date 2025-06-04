package com.jorami.eyeapp.auth.controller;
/**
 * Contrôleur REST exposant les endpoints d'authentification :
 * - Enregistrement
 * - Connexion
 * - Déconnexion
 * - Réinitialisation du mot de passe
 * - Vérification du code email
 */

// --- IMPORTS MÉTIER ---
import com.jorami.eyeapp.auth.model.*; // Modèles liés à l’auth (requests, tokens, verification codes)
import com.jorami.eyeapp.auth.service.AuthenticationService; // Logique d’enregistrement, login, reset
import com.jorami.eyeapp.auth.service.LogoutService; // Logique de déconnexion
import com.jorami.eyeapp.model.User; // Entité utilisateur
import com.jorami.eyeapp.service.EmailService; // Pour envoyer des emails (validation, reset)
import com.jorami.eyeapp.service.UserService; // Service pour trouver les utilisateurs en DB
import com.jorami.eyeapp.util.mapper.UserMapper; // Convertit User → DTO

// --- SWAGGER / OPENAPI ---
import io.swagger.v3.oas.annotations.tags.Tag; // Tag OpenAPI pour la documentation Swagger

// --- SERVLETS ---
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// --- VALIDATION ---
import jakarta.validation.Valid; // Pour valider automatiquement les objets reçus (email, password, etc.)

// --- LOMBOK ---
import lombok.RequiredArgsConstructor; // Génère automatiquement un constructeur avec tous les champs final

// --- SPRING ---
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; // Pour retourner des statuts HTTP + contenu JSON
import org.springframework.security.core.Authentication; // Objet représentant l’utilisateur authentifié
import org.springframework.web.bind.annotation.*; // Toutes les annotations REST : @RestController, @RequestBody, etc.

// --- UTILS ---
import java.io.UnsupportedEncodingException; // En cas de problème d’encodage dans l’envoi d’email
import java.util.Map;
import java.util.Optional; // Pour gérer les absences d'utilisateur

import static com.jorami.eyeapp.exception.ConstantMessage.*; // Constantes de messages pour la réponse

// --- ANNOTATIONS DE CLASSE ---

@CrossOrigin(
        origins = {"*"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
// Autorise les appels CORS à ce contrôleur, notamment pour Angular (localhost:4200 ou prod)

@RestController
// Déclare ce contrôleur comme composant REST (retourne des objets JSON)

@RequestMapping("auth")
// Préfixe de toutes les routes : /auth/...

@RequiredArgsConstructor
// Lombok injecte automatiquement les dépendances via constructeur (authenticationService, etc.)

@Tag(name = "Authentication")
// Annotation Swagger pour documenter ce contrôleur dans Swagger UI

public class AuthenticationController {

    // === Dépendances injectées ===
    private final AuthenticationService authenticationService;
    private final LogoutService logoutService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserMapper mapper;

    /**
     * Endpoint de test pour vérifier que l'API est en ligne.
     */
    @GetMapping("/health")
    public String getHealth() {
        return "The API is healthy";
    }

    /**
     * Enregistrement d’un nouvel utilisateur.
     * @param request Objet contenant tous les champs du formulaire d’inscription.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) throws UnsupportedEncodingException {
        authenticationService.register(request);
        return ResponseEntity.accepted().build(); // 202 Accepted
    }

    /**
     * Authentifie un utilisateur via email/password.
     * @param request Email + mot de passe
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authentication(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authentication(request));
    }

    /**
     * Déconnecte un utilisateur en invalidant son token JWT.
     */
    @PostMapping("/logout")
    public void logout(@RequestBody HttpServletRequest request,
                       @RequestBody HttpServletResponse response,
                       @RequestBody Authentication authentication) {
        logoutService.logout(request, response, authentication);
    }

    /**
     * Envoie un email contenant un code de validation pour le reset du mot de passe.
     */
    @PostMapping("/send-code-reset-password")
    public ResponseEntity<?> sendEmailCode(@RequestBody AuthenticationRequest request) throws UnsupportedEncodingException {
        Optional<User> user = userService.findUserByEmail(request);
        if (user.isPresent()) {
            authenticationService.confirmResetPassword(user.get());
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * Réinitialise le mot de passe via un code envoyé par mail.
     * @param resetRequest email + nouveau mot de passe + code
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetPasswordRequest resetRequest) {
        VerificationCode verificationCode = emailService.validateCodeExist(resetRequest.getCode());

        if (verificationCode != null) {
            AuthenticationRequest authReq = new AuthenticationRequest(resetRequest.getEmail(), resetRequest.getPassword());
            Optional<User> userOptional = userService.findUserByEmail(authReq);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                return ResponseEntity.ok(mapper.toUserDto(authenticationService.resetPassword(user, resetRequest)));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, UNAUTHORIZED_EMAIL_PASSWORD));
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, CODE_EMAIL_VALIDATION));
        }
    }

    /**
     * Vérifie un code de validation de compte envoyé par email (validation finale du compte).
     * @param code code reçu par email
     */
    @PutMapping("/verify-email/{code}")
    public ResponseEntity<?> verifyEmail(@PathVariable("code") String code) throws UnsupportedEncodingException {
        VerificationCode verificationCode = emailService.validateCodeExist(code);

        if (verificationCode != null) {
            authenticationService.validateCode(verificationCode);
            return ResponseEntity.ok(verificationCode);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(JSON_KEY, CODE_EMAIL_VALIDATION));
        }
    }
}
