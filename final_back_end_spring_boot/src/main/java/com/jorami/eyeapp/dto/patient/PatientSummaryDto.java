package com.jorami.eyeapp.dto.patient;

import com.jorami.eyeapp.dto.OrganizationDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * PatientSummaryDto est un DTO utilisé pour afficher une version simplifiée
 * d’un patient, notamment dans des listes ou résumés rapides.
 */
@NoArgsConstructor  // Génère un constructeur sans arguments (obligatoire pour certaines sérializations JSON)
@AllArgsConstructor // Génère un constructeur avec tous les champs
@Getter             // Génère automatiquement les getters pour tous les champs
@Setter             // Génère automatiquement les setters pour tous les champs
public class PatientSummaryDto {

    /** Identifiant unique du patient */
    private long id;

    /** Nom de famille du patient */
    private String lastname;

    /** Prénom du patient */
    private String firstname;

    /** Date de naissance (au format String, souvent yyyy-MM-dd) */
    private String birthDate;

    /** Version de l'objet pour la gestion de la concurrence (optimistic locking) */
    private long version;

    /** Liste des organisations (cliniques, hôpitaux, etc.) liées à ce patient */
    private List<OrganizationDto> organizations;

}
