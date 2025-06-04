package com.jorami.eyeapp.auth.service.implementation;

// Repository pour accéder aux utilisateurs
import com.jorami.eyeapp.repository.UserRepository;

// Permet de marquer la méthode comme transactionnelle (rollback automatique si exception)
import jakarta.transaction.Transactional;

// Lombok : génère automatiquement le constructeur avec tous les champs `final`
import lombok.RequiredArgsConstructor;

// Interfaces Spring Security
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// Déclare cette classe comme un composant métier Spring
import org.springframework.stereotype.Service;

// Constante personnalisée pour les messages d'erreurs
import static com.jorami.eyeapp.exception.ConstantMessage.USER_NOT_FOUND;

/**
 * Ce service implémente l’interface Spring Security `UserDetailsService`.
 * Il est utilisé par le système d’authentification Spring pour charger un utilisateur à partir de son email.
 * La méthode `loadUserByUsername` est appelée automatiquement au moment de l’authentification.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    // Dépendance injectée : accès aux utilisateurs via le repository
    private final UserRepository userRepository;

    /**
     * Cette méthode est invoquée automatiquement par Spring Security
     * lorsqu’un utilisateur tente de se connecter.
     * Elle récupère l’utilisateur en base à partir de son email (nom d’utilisateur).
     *
     * @param userEmail l’email fourni au moment du login
     * @return UserDetails : représentation Spring de l’utilisateur
     * @throws UsernameNotFoundException si l’email n’existe pas en base
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND));
    }
}
