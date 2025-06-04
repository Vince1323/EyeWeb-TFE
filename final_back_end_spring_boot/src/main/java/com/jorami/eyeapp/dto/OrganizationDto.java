package com.jorami.eyeapp.dto;

// Lombok permet de générer automatiquement les constructeurs, getters et setters
import lombok.AllArgsConstructor;  // Crée un constructeur avec tous les champs
import lombok.Getter;              // Crée automatiquement tous les getters
import lombok.NoArgsConstructor;   // Crée un constructeur vide (sans argument)
import lombok.Setter;              // Crée automatiquement tous les setters

/**
 * OrganizationDto est un objet de transfert de données (DTO)
 * utilisé pour transférer les données d'une organisation
 * entre le backend (Spring Boot) et le frontend (Angular).
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrganizationDto {

    /**
     * Identifiant unique de l'organisation.
     * Correspond généralement à la clé primaire en base de données.
     */
    private Long id;

    /**
     * Numéro de version de l'organisation.
     * Utilisé pour la gestion de la concurrence (optimistic locking).
     */
    private Long version;

    /**
     * Nom de l'organisation (ex : hôpital, cabinet, clinique...).
     */
    private String name;

    /**
     * Indique si l'organisation est globale (true) ou spécifique à un utilisateur (false).
     * Une organisation globale est accessible à tous les utilisateurs.
     */
    private Boolean isGlobal;
}
