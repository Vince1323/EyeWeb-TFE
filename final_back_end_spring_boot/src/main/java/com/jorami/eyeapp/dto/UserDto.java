package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;  // Génère automatiquement un constructeur avec tous les champs
import lombok.Getter;              // Génère les accesseurs (getters)
import lombok.NoArgsConstructor;   // Génère un constructeur sans arguments (nécessaire pour Jackson)
import lombok.Setter;              // Génère les mutateurs (setters)

import java.time.LocalDate;

/**
 * UserDto est un objet de transfert de données représentant un utilisateur de l’application
 * utilisé pour les échanges entre le backend et le frontend.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    /**
     * Identifiant unique de l’utilisateur
     */
    private long id;

    /**
     * Numéro de version de l’objet (utile pour le versionning ou les mises à jour optimistes)
     */
    private long version;

    /**
     * Prénom de l’utilisateur
     */
    private String firstname;

    /**
     * Nom de famille de l’utilisateur
     */
    private String lastname;

    /**
     * Adresse email de l’utilisateur
     */
    private String email;

    /**
     * Numéro de téléphone
     */
    private String phone;

    /**
     * Rôle de l’utilisateur (ex: ADMIN, DOCTOR)
     * Utilise un DTO spécifique pour éviter de passer l’entité complète
     */
    private RoleDto role;

    /**
     * Indique si l’utilisateur a accepté les conditions d’utilisation
     */
    private boolean hasReadTermsAndConditions;

    /**
     * Indique si l’utilisateur est vérifié (validé par un administrateur)
     */
    private boolean verified;

    /**
     * Indique si l’adresse email a été validée (par lien de validation)
     */
    private boolean validEmail;

    /**
     * Adresse postale de l’utilisateur, représentée par un objet AddressDto
     */
    private AddressDto address;

    /**
     * Date de naissance
     */
    private LocalDate birthDate;

    // Redéfinition explicite de l’accesseur si besoin d’une logique spéciale (ici non utilisée)
    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
