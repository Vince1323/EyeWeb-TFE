package com.jorami.eyeapp.dto;

// Importation des annotations Lombok pour éviter d’écrire manuellement getters, setters, etc.
import lombok.AllArgsConstructor;  // Génère un constructeur avec tous les champs
import lombok.Getter;              // Génère les getters pour tous les champs
import lombok.NoArgsConstructor;   // Génère un constructeur sans argument (nécessaire pour Jackson et Spring)
import lombok.Setter;              // Génère les setters pour tous les champs

/**
 * DTO (Data Transfer Object) qui représente la relation entre un utilisateur,
 * son organisation, et le fait qu’il soit ou non administrateur dans cette organisation.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserOrganizationRoleDto {

    /**
     * Représente l'utilisateur concerné (informations personnelles et rôle global)
     */
    private UserDto user;

    /**
     * Indique si l'utilisateur est administrateur dans l'organisation associée.
     * Ce rôle est spécifique à l'organisation (ex: admin de cette structure, mais pas d’une autre).
     */
    private Boolean isAdmin;

    /**
     * Organisation à laquelle l'utilisateur est rattaché.
     */
    private OrganizationDto organization;
}
