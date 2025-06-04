package com.jorami.eyeapp.auth.service.implementation;
/**
 * Implémentation concrète du service d’authentification (`AuthenticationService`).
 * Elle gère :
 * - l’enregistrement des utilisateurs
 * - l’authentification (login)
 * - l’envoi et la validation des codes d’email
 * - la gestion des tokens JWT (génération, révocation)
 * - la réinitialisation de mot de passe
 */

// Dépendances internes (modèles)
import com.jorami.eyeapp.auth.model.*;
import com.jorami.eyeapp.model.*;
import com.jorami.eyeapp.model.Enum;

// Repositories
import com.jorami.eyeapp.auth.repository.TokenRepository;
import com.jorami.eyeapp.repository.OrganizationRepository;
import com.jorami.eyeapp.repository.RoleRepository;
import com.jorami.eyeapp.repository.UserOrganizationRoleRepository;
import com.jorami.eyeapp.repository.VerificationCodeRepository;

// Services
import com.jorami.eyeapp.auth.service.AuthenticationService;
import com.jorami.eyeapp.auth.service.JwtService;
import com.jorami.eyeapp.service.EmailService;
import com.jorami.eyeapp.service.UserService;

// Strategies d’envoi d’email
import com.jorami.eyeapp.strategy.Implementation.RegistrationEmailStrategy;
import com.jorami.eyeapp.strategy.Implementation.ResetPasswordEmailStrategy;
import com.jorami.eyeapp.strategy.Implementation.ValidationEmailStrategy;
import com.jorami.eyeapp.strategy.Implementation.VerifiedAccountEmailStrategy;

// Utilitaires
import com.jorami.eyeapp.util.DateFunction;
import com.jorami.eyeapp.util.Utils;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

@Service // Indique que cette classe est un service métier Spring et sera gérée par le conteneur
@RequiredArgsConstructor // Génère automatiquement un constructeur avec tous les champs "final"
public class AuthenticationServiceImpl implements AuthenticationService {

    // Dépendances injectées automatiquement par Spring (injection via le constructeur)
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final OrganizationRepository organizationRepository;
    private final UserOrganizationRoleRepository userOrganizationRoleRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final VerificationCodeRepository verificationCodeRepository;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration; // Durée de validité des JWT (en millisecondes), injectée depuis le fichier de config

    // ----------------------
    // MÉTHODES PUBLIQUES
    // ----------------------

    /**
     * Inscription d'un utilisateur : crée un utilisateur, son organisation, son rôle, et envoie un email de validation.
     */
    @Override
    public void register(RegistrationRequest request) throws UnsupportedEncodingException {
        // 1. Récupération du rôle "USER" dans la base
        Role userRole = roleRepository.findRoleByName(Enum.UserRole.USER.toString())
                .orElseThrow(() -> new NoSuchElementException(ROLE_NOT_FOUND));

        // 2. Vérifie si un utilisateur avec cet email existe déjà
        Optional<User> existingUser = userService.findUserByEmail(request);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(USER_ALREADY_EXISTS);
        }

        // 3. Création du nouvel utilisateur avec les informations encodées
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .birthDate(DateFunction.parseDateLocalDate(request.getBirthDate()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .hasReadTermsAndConditions(request.isHasReadTermsAndConditions())
                .verified(false)
                .build();

        // 4. Création d’une organisation dédiée à cet utilisateur (isolé dans son propre espace)
        Organization organization = Organization.builder()
                .name(request.getEmail())
                .isGlobal(true)
                .build();

        // 5. Création du lien entre utilisateur et organisation avec le rôle admin (local)
        UserOrganizationRole userOrganizationRole = UserOrganizationRole.builder()
                .user(user).isAdmin(true)
                .organization(organization)
                .build();
        user.setUserOrganizationRoles(List.of(userOrganizationRole));

        // 6. Sauvegarde en base de l’utilisateur, de son organisation et du lien
        userService.saveUser(user);
        organizationRepository.save(organization);
        userOrganizationRoleRepository.save(userOrganizationRole);

        // 7. Envoi du mail de validation (code d’activation)
        this.confirmEmail(user);
    }

    /**
     * Authentifie un utilisateur et retourne un token JWT s’il est valide.
     */
    @Override
    public AuthenticationResponse authentication(AuthenticationRequest authenticationRequest) {
        return userService.findUserByEmail(authenticationRequest)
                .map(user -> {
                    if (!user.isVerified() || !user.isHasReadTermsAndConditions()) {
                        throw new AccessDeniedException(USER_ACCESS);
                    }
                    return authenticate(authenticationRequest);
                })
                .orElseThrow(() -> new BadCredentialsException(UNAUTHORIZED_EMAIL_PASSWORD));
    }

    /**
     * Envoie un email avec code de réinitialisation de mot de passe.
     */
    @Override
    public void confirmResetPassword(User user) throws UnsupportedEncodingException {
        if (user != null) {
            String code = this.generatesCode(user);
            ResetPasswordEmailStrategy strategy = new ResetPasswordEmailStrategy();
            emailService.sendEmail(user, code, strategy);
        }
    }

    /**
     * Valide le code de vérification reçu et confirme l'email de l'utilisateur.
     */
    @Override
    public void validateCode(VerificationCode verificationCode) throws UnsupportedEncodingException {
        User user = verificationCode.getUser();
        user.setValidEmail(true);
        userService.saveUser(user);
        this.confirmRegistrationEmail(user);
    }

    /**
     * Réinitialise le mot de passe d’un utilisateur après validation de code.
     */
    @Override
    public User resetPassword(User userSource, ResetPasswordRequest resetPasswordRequest) {
        try {
            userSource.setPassword(passwordEncoder.encode(resetPasswordRequest.getResetPassword()));
            userService.saveUser(userSource);
        } catch (OptimisticLockingFailureException e) {
            throw new OptimisticLockingFailureException(e.getMessage());
        }
        return userSource;
    }

    // ----------------------
    // MÉTHODES INTERNES PRIVÉES
    // ----------------------

    /**
     * Authentifie l'utilisateur (avec AuthenticationManager), génère et sauvegarde un token JWT.
     */
    private AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userService.findUserByEmailVerified(request);
        revokeAllUserTokens(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    /**
     * Enregistre un nouveau token JWT pour l’utilisateur connecté.
     */
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .expiredAt(LocalDateTime.now().plus(jwtExpiration, ChronoUnit.MILLIS))
                .build();
        tokenRepository.save(token);
    }

    /**
     * Révoque tous les tokens précédents de l’utilisateur (invalidation logique en base).
     */
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Génère un nouveau code de validation, supprime l'ancien s'il existe, puis le sauvegarde en base.
     */
    private String generatesCode(User user) {
        VerificationCode old = verificationCodeRepository.findByIdUser(user.getId());
        if (old != null) {
            verificationCodeRepository.delete(old);
            verificationCodeRepository.flush();
        }
        String code = Utils.generateCode();
        VerificationCode verificationCode = new VerificationCode(code, user);
        verificationCodeRepository.save(verificationCode);
        return code;
    }

    /**
     * Envoie un email contenant un code de validation à l’utilisateur.
     */
    private void confirmEmail(User user) throws UnsupportedEncodingException {
        String code = this.generatesCode(user);
        ValidationEmailStrategy strategy = new ValidationEmailStrategy();
        emailService.sendEmail(user, code, strategy);
    }

    /**
     * Envoie un email confirmant la validation du compte.
     */
    private void confirmRegistrationEmail(User user) throws UnsupportedEncodingException {
        RegistrationEmailStrategy strategy = new RegistrationEmailStrategy();
        emailService.sendEmail(user, null, strategy);
    }
}
