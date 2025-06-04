package com.jorami.eyeapp.auth.model;
/**
 * DTO utilisé pour la réinitialisation du mot de passe.
 * Il est envoyé au backend lorsque l'utilisateur a reçu un code par email et souhaite définir un nouveau mot de passe.
 */

// --- LOMBOK ---
import lombok.AllArgsConstructor;               // Génère un constructeur avec tous les champs
import lombok.Data;                             // Génère getters, setters, equals, hashCode, toString
import lombok.NoArgsConstructor;                // Génère un constructeur vide (requis pour la désérialisation JSON)
import lombok.experimental.SuperBuilder;        // Active le pattern builder, même pour les classes héritées

@Data
// Fournit automatiquement les getters, setters, toString, equals, hashCode

@SuperBuilder
// Permet de construire l’objet avec un builder : ResetPasswordRequest.builder().email(...).build()

@AllArgsConstructor
@NoArgsConstructor
// Constructeurs complet et vide : nécessaires pour Jackson, tests, etc.

public class ResetPasswordRequest {

    private String email;
    /**
     * Email de l’utilisateur qui demande la réinitialisation de son mot de passe.
     * Il est utilisé pour retrouver l’utilisateur correspondant en base de données.
     */

    private String password;
    /**
     * Ancien mot de passe (non utilisé ici pour validation).
     * Présent pour compatibilité éventuelle ou affichage frontend (peut être supprimé si inutile).
     */

    private String resetPassword;
    /**
     * Nouveau mot de passe à définir après vérification du code.
     * Ce champ sera validé en back-end avant d’être encodé et enregistré.
     */

    private String code;
    /**
     * Code de validation reçu par email par l’utilisateur.
     * Il permet de vérifier que la demande de réinitialisation est bien authentifiée.
     */
}
