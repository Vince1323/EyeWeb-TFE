package com.jorami.eyeapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * LensDto est un Data Transfer Object utilisé pour transporter
 * les données relatives à une lentille intraoculaire (IOL) dans l'application.
 * Il est utilisé côté backend pour envoyer des données au frontend de manière structurée.
 */
@NoArgsConstructor      // Génère un constructeur sans arguments (utilisé par frameworks comme Jackson)
@AllArgsConstructor     // Génère un constructeur avec tous les champs
@Getter                 // Génère automatiquement les getters pour tous les champs
@Setter                 // Génère automatiquement les setters pour tous les champs
public class LensDto {

    /** Identifiant unique de la lentille */
    private long id;

    /** Version utilisée pour la gestion de la concurrence (optimistic locking) */
    private long version;

    /** Nom commercial ou technique de la lentille (ex: "ZCB00", "SN60WF") */
    private String name;

    /** Nom de marque ou commentaire visible côté UI */
    private String commentTradeName;

    // 🔹 Paramètres de constants NOMINALES pour les formules de calcul de l'IOL

    /** Valeur nominale (si applicable, utilisée par certaines formules simples) */
    private Float nominal;

    /** Constantes Haigis (A0, A1, A2) utilisées dans la formule Haigis */
    private Float haigisA0;
    private Float haigisA1;
    private Float haigisA2;

    /** Constante utilisée par la formule Hoffer Q (PACD) */
    private Float hofferQPACD;

    /** Constante utilisée dans la formule Holladay 1 */
    private Float holladay1SF;

    /** Constante a utilisée dans la formule SRK/T */
    private Float srkta;

    /** Constantes de la formule Castrop (C = constante, H = hauteur, R = rayon) */
    private Float castropC;
    private Float castropH;
    private Float castropR;

    // 🔹 Paramètres de constants OPTIMISÉES (calculées à partir de données réelles)

    private Float haigisA0Optimized;
    private Float haigisA1Optimized;
    private Float haigisA2Optimized;

    private Float hofferQPACDOptimized;
    private Float holladay1SFOptimized;
    private Float cookeOptimized;  // Pour la formule Cooke K6

    private Float srktaOptimized;
    private Float castropCOptimized;
    private Float castropHOptimized;
    private Float castropROptimized;

    /** Fabricant de la lentille, encapsulé dans un objet LensManufacturerDto */
    private LensManufacturerDto lensManufacturer;

}
