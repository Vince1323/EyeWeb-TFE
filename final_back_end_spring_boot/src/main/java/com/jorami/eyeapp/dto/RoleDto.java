package com.jorami.eyeapp.dto;

// Lombok est utilisé pour générer automatiquement les méthodes de base
import lombok.AllArgsConstructor;  // Génère un constructeur avec tous les champs
import lombok.Getter;              // Génère automatiquement les getters
import lombok.NoArgsConstructor;   // Génère un constructeur vide (utile pour Jackson ou JPA)
import lombok.Setter;              // Génère automatiquement les setters

/**
 * RoleDto est un Data Transfer Object servant à transférer
 * le nom d’un rôle utilisateur (ex: ADMIN, USER) entre le backend et le frontend.
 */
@NoArgsConstructor  // Constructeur sans arguments
@AllArgsConstructor // Constructeur avec tous les arguments
@Getter             // Génère les accesseurs (getters)
@Setter             // Génère les mutateurs (setters)
public class RoleDto {

    /**
     * Nom du rôle (ex : "ADMIN", "USER", etc.)
     * Ce champ représente le rôle attribué à un utilisateur dans l’application.
     */
    private String name;
}
