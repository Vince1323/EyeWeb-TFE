package com.jorami.eyeapp.auth.model;
/**
 * Entité utilisée pour gérer les codes de validation envoyés par email
 * - Lors de l’inscription (validation du compte)
 * - Lors d’une réinitialisation de mot de passe
 * Chaque code est associé à un utilisateur, valable pendant 10 minutes.
 */

// --- ENTITÉS & JPA ---
import com.jorami.eyeapp.model.IdentifiedModel; // Classe abstraite contenant l'ID et potentiellement des champs d'audit
import com.jorami.eyeapp.model.User;           // Entité représentant l’utilisateur lié à ce code

import jakarta.persistence.*; // Annotations de persistance (Entity, Table, JoinColumn, etc.)

// --- LOMBOK ---
import lombok.AllArgsConstructor; // Génère un constructeur avec tous les champs
import lombok.Getter;             // Génère les getters
import lombok.NoArgsConstructor;  // Génère un constructeur vide (requis par JPA)
import lombok.Setter;             // Génère les setters

// --- JAVA ---
import java.util.Calendar; // Utilisé pour calculer l’expiration
import java.util.Date;     // Stocke la date/heure d’expiration

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "verification_code")
// Marque cette classe comme une entité persistée dans la table `verification_code`

public class VerificationCode extends IdentifiedModel {
    /**
     * Cette classe hérite d’IdentifiedModel :
     * - Cela signifie qu’elle hérite probablement d’un champ `id` (Long) et potentiellement des dates d’audit.
     */

    private String code;
    /**
     * Code de validation généré et envoyé à l’utilisateur par email.
     * Il est unique pour chaque utilisateur et valide temporairement (10 min).
     */

    private Date expirationTime;
    /**
     * Date et heure à laquelle le code expirera.
     * Calculée automatiquement lors de la création.
     */

    private static final int EXPIRATION_TIME = 10;
    /**
     * Durée de validité du code, en minutes.
     * Constante utilisée pour le calcul automatique de la date d’expiration.
     */

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    /**
     * Relation 1-1 avec l’utilisateur concerné.
     * Chaque utilisateur ne peut avoir qu’un seul code actif à la fois (à régénérer à chaque demande).
     * `@JoinColumn` spécifie que la clé étrangère est `user_id`.
     */

    /**
     * Constructeur personnalisé utilisé lors de la génération d’un nouveau code.
     * Il assigne directement le code, l’utilisateur, et calcule l’expiration.
     */
    public VerificationCode(String code, User user) {
        super(); // Appelle le constructeur de IdentifiedModel (pour init id, dates éventuelles)
        this.code = code;
        this.user = user;
        this.expirationTime = this.getCodeExpirationTime();
    }

    /**
     * Méthode utilitaire pour calculer la date/heure d’expiration du code.
     * Ajoute 10 minutes à l’heure système actuelle.
     */
    public Date getCodeExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime()); // Maintenant
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME); // +10 minutes
        return new Date(calendar.getTime().getTime()); // Résultat final
    }
}
