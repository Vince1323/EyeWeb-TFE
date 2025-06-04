package com.jorami.eyeapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jorami.eyeapp.model.Enum;
import com.jorami.eyeapp.model.Organization;
import com.jorami.eyeapp.model.Role;
import com.jorami.eyeapp.repository.OrganizationRepository;
import com.jorami.eyeapp.repository.RoleRepository;
import com.jorami.eyeapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import static com.jorami.eyeapp.exception.ConstantMessage.*;

/**
 * Classe de configuration centrale de l'application.
 * Fournit des beans nécessaires à l'infrastructure de sécurité (auth, encodeur),
 * client HTTP (WebClient), et initialise les rôles/organisations critiques au démarrage.
 */
@Configuration // Indique à Spring que cette classe contient des beans à enregistrer dans le contexte
@RequiredArgsConstructor // Génère un constructeur pour injecter les dépendances final
public class ApplicationConfig {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final OrganizationRepository organizationRepository;

    /**
     * Bean permettant de charger un utilisateur à partir de son email (username).
     * Nécessaire à Spring Security pour l'authentification.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }

    /**
     * Bean définissant la méthode d'authentification (ici via base de données).
     * Utilise notre UserDetailsService + encodeur de mot de passe.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Bean définissant l’algorithme de hachage des mots de passe.
     * BCrypt est sécurisé et adapté aux bonnes pratiques actuelles.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Fournit le gestionnaire d’authentification utilisé par Spring Security.
     * Nécessaire pour pouvoir appeler authenticationManager.authenticate() manuellement.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean WebClient avec configuration JSON personnalisée.
     * Sert à faire des appels HTTP vers d'autres services (API d’implants par exemple).
     */
    @Bean
    public WebClient webClient() {
        ObjectMapper objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL) // ignore les champs nulls
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // formats de dates ISO

        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON)))
                .build();
    }

    /**
     * Méthode appelée au démarrage de l'application pour créer des rôles par défaut
     * (ADMIN, USER) et une organisation Citadelle si elle n'existe pas.
     */
    @Bean
    @Transactional
    public CommandLineRunner initializeRoles() {
        return args -> {
            String adminRoleName = Enum.UserRole.ADMIN.toString();
            if(roleRepository.findRoleByName(adminRoleName).isEmpty()) {
                Role adminRole = Role.builder()
                        .name(adminRoleName)
                        .deleted(false)
                        .build();
                roleRepository.save(adminRole);
            }
            String userRoleName = Enum.UserRole.USER.toString();
            if(roleRepository.findRoleByName(userRoleName).isEmpty()) {
                Role userRole = Role.builder()
                        .name(userRoleName)
                        .deleted(false)
                        .build();
                roleRepository.save(userRole);
            }
            if(organizationRepository.findOrganizationByUserEmail("Citadelle - Hôpital public de Liège") == null) {
                Organization organization = Organization.builder()
                        .isGlobal(false)
                        .name("Citadelle - Hôpital public de Liège")
                        .build();
                organizationRepository.save(organization);
            }
        };
    }
}
