package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) représentant une constante utilisée
 * dans les calculs biométriques, par exemple pour des formules IOL.
 */
@NoArgsConstructor  // Génère un constructeur vide (utile pour la désérialisation JSON)
@AllArgsConstructor // Génère un constructeur avec tous les champs
@Getter             // Génère tous les getters
@Setter             // Génère tous les setters
public class ConstantDto {

    /**
     * Identifiant unique de la constante.
     * Permet de faire le lien avec une entité persistée si besoin.
     */
    private long id;

    /**
     * Numéro de version (utilisé pour le contrôle de version,
     * par exemple avec Optimistic Locking).
     */
    private long version;

    /**
     * Type de constante (ex : "A-constant", "SF", "Holladay", etc.).
     * Cela permet d’identifier la nature de la constante.
     */
    private String constantType;

    /**
     * Valeur de la constante sous forme de chaîne (ex : "118.4").
     * Peut contenir des valeurs numériques ou textuelles selon le type.
     */
    private String value;

    /**
     * Formule à laquelle la constante est associée (ex : "SRK/T", "Haigis").
     * Permet de regrouper les constantes par formule biométrique.
     */
    private String formula;
}
