package com.jorami.eyeapp.auth.service.implementation;

// Modèle de token JWT
import com.jorami.eyeapp.auth.model.Token;
// Accès aux tokens stockés en base
import com.jorami.eyeapp.auth.repository.TokenRepository;
// Interface du service JWT à implémenter
import com.jorami.eyeapp.auth.service.JwtService;
// Entité utilisateur
import com.jorami.eyeapp.model.User;

// Librairie jjwt : création, parsing, signature des tokens JWT
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Spring
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service // Indique que ce composant est un service métier géré par Spring
@RequiredArgsConstructor // Génère un constructeur avec toutes les dépendances `final`
public class JwtServiceImpl implements JwtService {

    // Injecté depuis application.yaml → durée de validité du JWT (en ms)
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // Clé secrète codée en base64 utilisée pour signer les tokens JWT
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    // Accès aux tokens en base pour vérification (revocation, validité)
    private final TokenRepository tokenRepository;

    /**
     * Vérifie si le token correspond bien à l'utilisateur, et qu’il n’est pas expiré.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Vérifie que le token est bien enregistré en base (dans table token).
     * On retire les 7 premiers caractères du header "Bearer "
     */
    @Override
    public boolean validToken(String token) {
        Token tokenFind = tokenRepository.findByToken(token.substring(7)).orElse(null);
        return (tokenFind != null);
    }

    /**
     * Extrait le username (ici : email) à partir du token JWT.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Méthode générique pour extraire n’importe quel champ (claim) du token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Génère un JWT signé pour un utilisateur donné.
     * Le token inclut des informations utiles (claims) dans le payload.
     */
    public String generateToken(UserDetails userDetails) {
        User user = (User) userDetails;
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("firstname", user.getFirstname());
        claims.put("lastname", user.getLastname());
        claims.put("role", user.getRole().getName());
        claims.put("hasReadTermsAndConditions", user.isHasReadTermsAndConditions());
        return generateToken(claims, userDetails);
    }

    /**
     * Génère un JWT à partir de claims et d’un utilisateur.
     */
    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpiration);
    }

    /**
     * Construction complète du JWT avec header, payload et signature.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long jwtExpiration) {
        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Récupère la clé secrète sous forme de clé cryptographique pour signer le JWT.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Vérifie si le token a expiré.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Récupère la date d’expiration du token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Récupère tous les claims présents dans le JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrait le rôle contenu dans les claims du JWT.
     */
    private String extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return (String) claims.get("role");
    }

    /**
     * Reconstruit une autorité Spring Security à partir du rôle contenu dans le JWT.
     */
    public SimpleGrantedAuthority getAuthoritie(String token) {
        String role = extractRoles(token);
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}
