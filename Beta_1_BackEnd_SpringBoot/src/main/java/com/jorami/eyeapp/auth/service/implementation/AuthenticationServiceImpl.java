package com.jorami.eyeapp.auth.service.implementation;

import com.jorami.eyeapp.auth.model.*;
import com.jorami.eyeapp.auth.repository.TokenRepository;
import com.jorami.eyeapp.auth.service.AuthenticationService;
import com.jorami.eyeapp.auth.service.JwtService;
import com.jorami.eyeapp.model.*;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.repository.OrganizationRepository;
import com.jorami.eyeapp.repository.RoleRepository;
import com.jorami.eyeapp.repository.UserOrganizationRoleRepository;
import com.jorami.eyeapp.repository.VerificationCodeRepository;
import com.jorami.eyeapp.service.EmailService;
import com.jorami.eyeapp.service.UserService;
import com.jorami.eyeapp.strategy.Implementation.RegistrationEmailStrategy;
import com.jorami.eyeapp.strategy.Implementation.ResetPasswordEmailStrategy;
import com.jorami.eyeapp.strategy.Implementation.ValidationEmailStrategy;
import com.jorami.eyeapp.strategy.Implementation.VerifiedAccountEmailStrategy;
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

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

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
    private long jwtExpiration;


    /**
     * @param request
     */
    @Override
    public void register(RegistrationRequest request) throws UnsupportedEncodingException {
        Role userRole = roleRepository.findRoleByName(Enum.UserRole.USER.toString())
                .orElseThrow(() -> new NoSuchElementException(ROLE_NOT_FOUND));

        Optional<User> existingUser = userService.findUserByEmail(request);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(USER_ALREADY_EXISTS);
        }
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
        Organization organization = Organization.builder()
                .name(request.getEmail())
                .isGlobal(true)
                .build();
        UserOrganizationRole userOrganizationRole = UserOrganizationRole.builder()
                .user(user).isAdmin(true)
                .organization(organization)
                .build();
        user.setUserOrganizationRoles(List.of(userOrganizationRole));

        userService.saveUser(user);
        organizationRepository.save(organization);
        userOrganizationRoleRepository.save(userOrganizationRole);
        this.confirmEmail(user);
    }

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

    @Override
    public void confirmResetPassword(User user) throws UnsupportedEncodingException {
        if(user != null) {
            String code = this.generatesCode(user);
            ResetPasswordEmailStrategy resetPasswordEmailStrategy = new ResetPasswordEmailStrategy();
            emailService.sendEmail(user, code, resetPasswordEmailStrategy);
        }
    }

    @Override
    public void validateCode(VerificationCode verificationCode) throws UnsupportedEncodingException {
        User user = verificationCode.getUser();
        user.setValidEmail(true);
        userService.saveUser(user);
        this.confirmRegistrationEmail(user);
    }

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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

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

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private String generatesCode(User user) {
        VerificationCode verificationCode = verificationCodeRepository.findByIdUser(user.getId());
        if(verificationCode != null) {
            verificationCodeRepository.delete(verificationCode);
            verificationCodeRepository.flush();
        }
        String code = Utils.generateCode();
        verificationCode = new VerificationCode(code, user);
        verificationCodeRepository.save(verificationCode);
        return code;
    }

    private void confirmEmail(User user) throws UnsupportedEncodingException {
        String code = this.generatesCode(user);
        ValidationEmailStrategy validationEmailStrategy = new ValidationEmailStrategy();
        emailService.sendEmail(user, code, validationEmailStrategy);
    }

    private void confirmRegistrationEmail(User user) throws UnsupportedEncodingException {
        RegistrationEmailStrategy registrationEmailStrategy = new RegistrationEmailStrategy();
        emailService.sendEmail(user, null, registrationEmailStrategy);
    }

}
