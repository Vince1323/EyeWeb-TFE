package com.jorami.eyeapp.auth.repository;
/**
 * Interface Spring Data JPA pour accéder aux tokens JWT persistés en base de données.
 * Permet :
 *   - de retrouver un token précis,
 *   - de récupérer tous les tokens actifs pour un utilisateur.
 */

import com.jorami.eyeapp.auth.model.Token; // Entité liée à cette interface
import org.springframework.data.jpa.repository.JpaRepository; // Interface de base fournie par Spring Data JPA
import org.springframework.data.jpa.repository.Query;        // Pour définir des requêtes JPQL personnalisées

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    /**
     * Hérite des méthodes de JpaRepository :
     * - findAll(), findById(), save(), delete(), etc.
     * - On spécifie ici : <Token, Long> (entité, type ID)
     */

    Optional<Token> findByToken(String token);
    /**
     * Méthode dérivée automatiquement par Spring Data JPA.
     * Recherche un token dans la base sur base de sa valeur exacte (colonne `token`).
     * @param token Le JWT complet à rechercher.
     * @return Optional<Token> (peut ne pas exister).
     */

    @Query(value = """
        SELECT t FROM Token t INNER JOIN User u 
        ON t.user.id = u.id 
        WHERE u.id = :id AND (t.expired = false OR t.revoked = false)
    """)
    List<Token> findAllValidTokenByUser(Long id);
    /**
     * Requête JPQL personnalisée :
     * - Récupère tous les tokens valides (non expirés ET/OU non révoqués)
     *   pour un utilisateur donné.
     * - Utilisée dans la méthode `revokeAllUserTokens` pour invalider tous les anciens tokens
     *   d’un utilisateur avant de lui en générer un nouveau.
     *
     * @param id identifiant de l’utilisateur
     * @return liste des tokens actifs en base pour cet utilisateur
     */
}
