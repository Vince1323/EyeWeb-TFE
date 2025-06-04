package com.jorami.eyeapp.auth.model;
/**
 * Modèle utilisé pour recevoir les données du formulaire de connexion (email et mot de passe).
 * Ce DTO (Data Transfer Object) est utilisé lors de l'appel POST `/auth/authenticate`.
 */

// --- VALIDATION JAKARTA (ex-JSR 380) ---
import com.jorami.eyeapp.exception.ConstantMessage; // Fichier contenant les messages de validation personnalisés
import jakarta.validation.constraints.Email; // Vérifie que la chaîne est bien un email
import jakarta.validation.constraints.NotBlank; // Champ obligatoire, non null, non vide
import jakarta.validation.constraints.Size; // Contraint la taille de la chaîne

// --- LOMBOK ---
import lombok.AllArgsConstructor; // Génère un constructeur avec tous les champs
import lombok.Data; // Génère les getters, setters, equals, hashCode, toString
import lombok.NoArgsConstructor; // Génère un constructeur vide (nécessaire pour la désérialisation)
import lombok.experimental.SuperBuilder; // Génère un builder compatible avec l’héritage (utile pour d'autres DTOs hérités)

@Data
// Lombok : ajoute automatiquement les méthodes getters/setters, equals/hashCode, toString

@SuperBuilder
// Lombok : permet de créer facilement des objets avec un builder (même en cas d'héritage)

@AllArgsConstructor
// Lombok : génère un constructeur avec tous les champs

@NoArgsConstructor
// Lombok : génère un constructeur vide (obligatoire pour Jackson lors du mapping JSON → Java)

public class AuthenticationRequest {
    // === CHAMPS ===

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Email(message = ConstantMessage.VALIDATION_EMAIL)
    private String email;
    /**
     * Email de l’utilisateur, utilisé comme identifiant unique.
     * - Non vide
     * - Format email valide (ex. user@example.com)
     * Les messages d’erreur sont définis dans la classe `ConstantMessage`.
     */

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    @Size(min = 8, message = ConstantMessage.VALIDATION_PASSWORD_MIN_LENGTH)
    @Size(max = 100, message = ConstantMessage.VALIDATION_PASSWORD_MAX_LENGTH)
    private String password;
    /**
     * Mot de passe de l’utilisateur.
     * - Doit faire entre 8 et 100 caractères.
     * - Ne peut pas être vide.
     * La sécurité impose un minimum de 8 caractères pour limiter les attaques par force brute.
     */
}
