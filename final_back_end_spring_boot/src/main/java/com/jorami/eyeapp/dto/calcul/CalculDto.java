package com.jorami.eyeapp.dto.calcul;

// Jackson permet de sérialiser/désérialiser des objets Java en JSON (et inversement)
import com.fasterxml.jackson.annotation.JsonProperty;

// Imports de DTO nécessaires pour construire un CalculDto
import com.jorami.eyeapp.dto.ConstantDto;
import com.jorami.eyeapp.dto.DiopterDto;
import com.jorami.eyeapp.dto.LensDto;
import com.jorami.eyeapp.dto.exam.ExamDto;

// Lombok génère automatiquement les getters, setters, constructeur sans arguments et avec tous les arguments
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO représentant un calcul de puissance pour l’implantation d’une lentille intraoculaire (IOL).
 * Ce DTO est utilisé pour transférer les données entre le front-end (Angular) et le back-end.
 */
@NoArgsConstructor // Génère un constructeur vide
@AllArgsConstructor // Génère un constructeur avec tous les champs
@Getter // Génère les getters pour tous les champs
@Setter // Génère les setters pour tous les champs
public class CalculDto {

    /**
     * Côté de l’œil concerné (OD ou OS)
     */
    private String eyeSide;

    /**
     * Biométrie (examens) utilisée pour le calcul
     */
    private ExamDto exam;

    /**
     * Lentille choisie pour le calcul
     */
    private LensDto lens;

    /**
     * Liste des constantes biométriques (A-constant, SF, etc.)
     */
    @JsonProperty("constants") // Permet de nommer explicitement la propriété en JSON
    private List<ConstantDto> constants;

    /**
     * Liste des puissances possibles (dioptries) à tester
     */
    private List<DiopterDto> diopters;

    /**
     * Réfraction cible souhaitée (souvent 0.00, -0.25 ou -0.50 D)
     */
    private Float targetRefraction;

    /**
     * Indique si ce calcul concerne le deuxième œil du patient
     */
    private Boolean isSecondEye;

    /**
     * Puissance sélectionnée par l’utilisateur (dioptrie finale choisie)
     */
    private Float precPowerSelected;

    /**
     * Sphère équivalente (SE) du calcul
     */
    private Float se;

    /**
     * ID de l’examen précédent utilisé comme référence
     */
    private Long idReferencePrecExam;

    /**
     * ID du calcul précédent utilisé comme référence
     */
    private Long idReferencePrecCalcul;
}
