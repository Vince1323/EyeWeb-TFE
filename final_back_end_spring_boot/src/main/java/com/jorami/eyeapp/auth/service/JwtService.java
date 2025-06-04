package com.jorami.eyeapp.auth.service;

// Librairie utilisée pour manipuler les claims (informations) des JWT
import io.jsonwebtoken.Claims;

// Représente une autorité (rôle) dans Spring Security
import org.springframework.security.core.authority.SimpleGrantedAuthority;

// Représente un utilisateur Spring Security
import org.springframework.security.core.userdetails.UserDetails;

// Permet de passer une fonction lambda comme paramètre (ex : récupérer expiration, subject, etc.)
import java.util.function.Function;

/**
 * Interface de service utilisée pour toutes les opérations sur les tokens JWT.
 * Ce service encapsule la génération, la validation, l’analyse et l’extraction des informations du token.
 */
public interface JwtService {

    /**
     * Vérifie si un token JWT est valide pour un utilisateur donné :
     * - Le username dans le token correspond à celui de l’utilisateur
     * - Le token n’est pas expiré
     *
     * @param token         le token JWT à vérifier
     * @param userDetails   l’utilisateur à comparer
     * @return true si le token est valide, false sinon
     */
    boolean isTokenValid(String token, UserDetails userDetails);

    /**
     * Vérifie uniquement si le token existe dans la base (et n’est pas falsifié),
     * sans vérifier son contenu (claims).
     *
     * @param token le token JWT
     * @return true si le token est connu de la base
     */
    boolean validToken(String token);

    /**
     * Extrait le nom d’utilisateur (souvent l’email) à partir du token JWT.
     * C’est souvent le claim `sub` (subject).
     *
     * @param token le token JWT
     * @return le nom d’utilisateur présent dans le token
     */
    String extractUsername(String token);

    /**
     * Extrait n’importe quel claim à partir du token JWT,
     * en appliquant une fonction sur les claims du token.
     * Exemple d’utilisation : `extractClaim(token, Claims::getExpiration)`
     *
     * @param token le JWT
     * @param claimResolver la fonction à appliquer sur les claims
     * @param <T> le type de valeur extraite
     * @return la valeur extraite (ex : date d’expiration, email, rôle…)
     */
    <T> T extractClaim(String token, Function<Claims, T> claimResolver);

    /**
     * Génére un token JWT contenant les claims nécessaires pour un utilisateur donné.
     * Ce token inclut : subject, date d’expiration, et autorités.
     *
     * @param userDetails les détails de l’utilisateur (nom, email, rôles…)
     * @return le token signé au format JWT
     */
    String generateToken(UserDetails userDetails);

    /**
     * Extrait le rôle de l’utilisateur depuis le token JWT
     * et le convertit en autorité Spring (`ROLE_USER`, `ROLE_ADMIN`, etc.)
     *
     * @param token le token JWT
     * @return l’autorité principale contenue dans le token
     */
    SimpleGrantedAuthority getAuthoritie(String token);
}
