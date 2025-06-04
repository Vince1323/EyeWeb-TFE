package com.jorami.eyeapp.auth.model;
/**
 * Entité représentant un token JWT associé à un utilisateur.
 * Elle permet de suivre la validité du token (expiré, révoqué), et de gérer la déconnexion et la sécurité stateless.
 * Table SQL : `token`
 */

// --- ENTITÉS & JPA ---
import com.jorami.eyeapp.model.User; // Lien vers l'utilisateur propriétaire du token
import jakarta.persistence.*;         // Annotations JPA pour la persistance (Entity, Id, Column, etc.)

// --- LOMBOK ---
import lombok.*;                      // Génère automatiquement les getters, setters, constructeurs, etc.

// --- SPRING DATA AUDITING ---
import org.springframework.data.annotation.CreatedDate; // Pour enregistrer automatiquement la date de création
import org.springframework.data.jpa.domain.support.AuditingEntityListener; // Pour activer les champs @CreatedDate

// --- JAVA ---
import java.time.LocalDateTime; // Pour gérer les dates de création et expiration

@Getter
@Setter
// Lombok : Génère automatiquement les accesseurs pour tous les champs

@Builder
// Lombok : Permet de construire des objets avec `.builder()` (ex: Token.builder().token(...).build())

@AllArgsConstructor
@NoArgsConstructor
// Lombok : Constructeurs complet et vide (utiles pour JPA et frameworks)

@Entity
// Marque cette classe comme une entité persistée (gérée par JPA et enregistrée dans la base de données)

@Table(name = "token")
// Nom explicite de la table (token)

@EntityListeners({AuditingEntityListener.class})
// Active les fonctionnalités d'audit : @CreatedDate, etc.

public class Token {

    @Id
    @GeneratedValue
    private Long id;
    /**
     * Identifiant unique du token (clé primaire auto-générée).
     */

    @Column(unique = true, length = 500)
    public String token;
    /**
     * Chaîne représentant le token JWT.
     * - Unique : on ne doit pas stocker plusieurs fois le même token.
     * - Longueur max 500 caractères (suffisante pour des JWT encodés en base64).
     */

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    /**
     * Date de création automatique du token (gérée par Spring via Auditing).
     * - Non modifiable après création (updatable = false).
     */

    @Column(updatable = false)
    private LocalDateTime expiredAt;
    /**
     * Date d’expiration logique du token.
     * - Gérée lors de la génération (ex: ajout de X minutes à now).
     * - Peut être utilisée pour invalider un token même s’il n’est pas expiré côté JWT.
     */

    public boolean revoked;
    /**
     * Si true → le token a été explicitement révoqué (ex: logout).
     * Permet de rendre le token invalide sans attendre son expiration.
     */

    public boolean expired;
    /**
     * Si true → le token est considéré comme expiré (en base, pas forcément au niveau JWT).
     * Double sécurité avec la date.
     */

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    public User user;
    /**
     * Lien vers l’utilisateur auquel ce token appartient.
     * - Relation ManyToOne : un utilisateur peut avoir plusieurs tokens (ex: multi device).
     * - Clé étrangère : user_id
     */
}
