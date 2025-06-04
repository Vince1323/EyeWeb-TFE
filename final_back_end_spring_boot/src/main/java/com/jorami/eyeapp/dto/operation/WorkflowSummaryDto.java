package com.jorami.eyeapp.dto.operation;

// Lombok génère automatiquement les getters/setters pour tous les champs
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO (Data Transfer Object) utilisé pour représenter un résumé opératoire
 * d’un workflow chirurgical. Contient les principales informations médicales
 * et organisationnelles autour d’une opération.
 */
@Getter
@Setter
public class WorkflowSummaryDto {

    /** Identifiant du résumé opératoire */
    private Long id;

    /** Identifiant du workflow chirurgical associé (clé étrangère logique) */
    private Long workflowId;

    /** Antécédents oculaires */
    private String atcdO;

    /** Historique médical général */
    private String medicalHistory;

    /** Oeil dominant (OD / OS) */
    private String dominantEye;

    /** Allergies du patient (ex. médicaments) */
    private String allergy;

    /** Type d'anesthésie prévue ou utilisée */
    private String anesthesia;

    /** Type d'intervention chirurgicale (ex. cataracte, rétine, etc.) */
    private String surgeryType;

    /** Date programmée ou réalisée de la chirurgie */
    private LocalDate dateSurgery;

    /** Latéralité (OD, OS, OU = œil droit, gauche, ou les deux) */
    private String laterality;

    /** Nom du chirurgien */
    private String surgeon;

    /** Type de consultation préopératoire avec le chirurgien */
    private String visitWithSurgeon;

    /** Informations sur le cristallin artificiel (IOL) implanté */
    private String iol;

    /** Liste des matériaux opératoires utilisés (fils, vis, implants, etc.) */
    private List<String> operatingMaterials;

    /** Liste des traitements préopératoires prescrits */
    private List<String> preopTreatments;

    /** Liste des rendez-vous postopératoires prévus */
    private List<String> postopAppointments;
}
