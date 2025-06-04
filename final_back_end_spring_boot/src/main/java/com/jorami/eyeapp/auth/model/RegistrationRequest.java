package com.jorami.eyeapp.auth.model;
/**
 * DTO utilisé pour enregistrer un nouvel utilisateur via le endpoint `/auth/register`.
 * Il hérite de `AuthenticationRequest` pour inclure email + mot de passe,
 * et ajoute les champs nécessaires à l'inscription : prénom, nom, date de naissance, adresse, etc.
 */

// --- IMPORTS MÉTIER ---
import com.jorami.eyeapp.exception.ConstantMessage; // Contient les messages d'erreur pour les annotations de validation
import com.jorami.eyeapp.model.Address;             // Classe représentant l’adresse d’un utilisateur (entité persistée)

// --- VALIDATION ---
import jakarta.validation.constraints.NotBlank; // Valide que les chaînes ne sont ni nulles ni vides

// --- LOMBOK ---
import lombok.*;                     // Import global des annotations Lombok (Getter, Setter, etc.)
import lombok.experimental.SuperBuilder; // Permet d'utiliser le Builder même en cas d'héritage (superclasse AuthenticationRequest)

@Getter
@Setter
// Génère les getters et setters pour tous les champs

@SuperBuilder
// Fournit un builder compatible avec l’héritage : RegistrationRequest.builder().email(...).firstname(...).build()

@AllArgsConstructor
@NoArgsConstructor
// Constructeurs avec et sans arguments requis par Jackson

public class RegistrationRequest extends AuthenticationRequest {
    // Hérite déjà de : email, password (et validations associées)

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    private String firstname;
    /**
     * Prénom de l’utilisateur. Ne doit pas être vide.
     * La validation s’appuie sur une constante personnalisée de message d’erreur.
     */

    @NotBlank(message = ConstantMessage.VALIDATION_NOT_BLANK)
    private String lastname;
    /**
     * Nom de famille de l’utilisateur. Obligatoire pour l’inscription.
     */

    private String birthDate;
    /**
     * Date de naissance au format String (sera convertie en LocalDate dans le service).
     * Pourquoi String ? Pour faciliter le parsing depuis le JSON, puis transformation avec DateFunction.
     */

    private String phone;
    /**
     * Numéro de téléphone (optionnel ici, mais utile pour contact ou double authentification).
     */

    @NonNull
    private Address address;
    /**
     * Adresse complète de l'utilisateur, représentée par une entité persistée `Address`.
     * Annotée avec `@NonNull` pour s'assurer qu'elle est présente au moment de l'enregistrement.
     */

    private boolean hasReadTermsAndConditions;
    /**
     * Flag utilisé pour s’assurer que l’utilisateur a bien accepté les conditions générales.
     * Obligatoire pour valider l’inscription (contrôlé en backend avant création du compte).
     */
}
