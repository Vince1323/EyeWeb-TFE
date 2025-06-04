package com.jorami.eyeapp.dto;

// Lombok permet de générer automatiquement les constructeurs, getters et setters pour alléger le code Java
import lombok.AllArgsConstructor;  // Génère un constructeur avec tous les champs
import lombok.Getter;              // Génère les getters
import lombok.NoArgsConstructor;   // Génère un constructeur sans argument (utilisé par les frameworks)
import lombok.Setter;              // Génère les setters

/**
 * LensManufacturerDto est un Data Transfer Object (DTO) servant à transférer
 * les données relatives à un fabricant de lentilles intraoculaires entre
 * les couches backend et frontend.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LensManufacturerDto {

    /**
     * Identifiant unique du fabricant.
     * Utilisé pour distinguer chaque fabricant en base de données.
     */
    private long id;

    /**
     * Version du fabricant.
     * Sert à la gestion de la concurrence optimiste (optimistic locking).
     */
    private long version;

    /**
     * Nom du fabricant (ex : Johnson & Johnson, Zeiss, Alcon, etc.)
     */
    private String name;
}
