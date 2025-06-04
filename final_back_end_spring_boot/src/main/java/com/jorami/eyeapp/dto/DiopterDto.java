package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) représentant une ligne de calcul de dioptrie.
 * Utilisé pour transmettre une combinaison {puissance IOL, valeur de sortie, formule} au frontend.
 */
@NoArgsConstructor      // Génère un constructeur sans argument (utilisé par Jackson, etc.)
@AllArgsConstructor     // Génère un constructeur avec tous les champs
@Getter                 // Génère automatiquement tous les getters
@Setter                 // Génère automatiquement tous les setters
public class DiopterDto {

    /**
     * Identifiant unique du calcul de dioptrie (utile si on sauvegarde en BDD).
     */
    private long id;

    /**
     * Version utilisée pour le contrôle de concurrence (Optimistic Locking).
     */
    private long version;

    /**
     * Puissance de l’implant intraoculaire (IOL) testée, en dioptries.
     * Exemple : 22.5, 23.0, etc.
     */
    private Float iolPower;

    /**
     * Valeur calculée par la formule (ex: prédiction de réfraction post-opératoire).
     * Exemple : -0.25 D, +0.10 D, etc.
     */
    private Float value;

    /**
     * Formule utilisée pour le calcul de la valeur (ex : "SRK/T", "Haigis", etc.).
     */
    private String formula;
}
