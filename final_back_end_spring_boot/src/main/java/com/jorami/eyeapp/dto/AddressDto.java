package com.jorami.eyeapp.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * AddressDto est un Data Transfer Object utilisé pour transporter
 * les informations d'adresse d'un patient ou d'un utilisateur.
 */
@NoArgsConstructor  // Génère un constructeur vide (nécessaire pour la désérialisation JSON)
@AllArgsConstructor // Génère un constructeur avec tous les champs comme arguments
@Getter             // Génère automatiquement les getters pour tous les champs
@Setter             // Génère automatiquement les setters pour tous les champs
public class AddressDto {

    /** Identifiant unique de l’adresse */
    private Long id;

    /** Version utilisée pour le contrôle optimiste des versions (optimistic locking) */
    private Long version;

    /** Rue (ex. : "Avenue des Lilas") */
    private String street;

    /** Numéro de rue (ex. : "12") */
    private String streetNumber;

    /** Numéro de boîte éventuel (ex. : "3B") */
    private String boxNumber;

    /** Code postal (ex. : "4000") */
    private String zipCode;

    /** Ville (ex. : "Liège") */
    private String city;

    /** Pays (ex. : "Belgique") */
    private String country;

}
