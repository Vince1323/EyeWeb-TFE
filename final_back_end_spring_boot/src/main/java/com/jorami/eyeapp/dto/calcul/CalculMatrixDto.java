package com.jorami.eyeapp.dto.calcul;

// Gère la sérialisation JSON, ici pour renommer des champs dans le JSON généré
import com.fasterxml.jackson.annotation.JsonProperty;

import com.jorami.eyeapp.dto.ConstantDto;
import com.jorami.eyeapp.dto.LensDto;
import com.jorami.eyeapp.dto.exam.ExamDto;

// Lombok : annotations pour générer automatiquement constructeur, getters, setters
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * CalculMatrixDto regroupe toutes les données nécessaires pour générer une matrice de résultats
 * de calculs d’implant (puissances, formules, valeurs, etc.).
 * Ce DTO est utilisé notamment dans l’interface de sélection d’implant.
 */
@NoArgsConstructor // Génère un constructeur sans argument
@AllArgsConstructor // Génère un constructeur avec tous les champs
@Getter // Génère tous les getters
@Setter // Génère tous les setters
public class CalculMatrixDto {

    /**
     * Identifiant unique du calcul
     */
    private Long id;

    /**
     * Côté de l’œil concerné (ex: OD = œil droit, OS = œil gauche)
     */
    private String eyeSide;

    /**
     * Examen biométrique utilisé pour effectuer le calcul
     */
    private ExamDto exam;

    /**
     * Lentille utilisée pour le calcul
     */
    private LensDto lens;

    /**
     * Liste des constantes biométriques utilisées pour les calculs
     */
    @JsonProperty("constants")
    private List<ConstantDto> constants;

    /**
     * Identifiant du patient concerné par ce calcul
     */
    private Long patientId;

    /**
     * Réfraction visée après l'opération (souvent 0.00D, -0.25D, etc.)
     */
    private Float targetRefraction;

    /**
     * Liste des noms de formules utilisées pour le calcul (ex: Haigis, SRK/T, Holladay, etc.)
     */
    private List<String> formulas;

    /**
     * Liste des puissances (dioptries) testées ou proposées dans le calcul
     */
    private List<Float> powers;

    /**
     * Matrice des résultats des formules (chaque ligne = une formule, chaque colonne = une puissance)
     * Exemple : valueMatrix[i][j] = résultat de la formule i avec la puissance j
     */
    private List<Float[]> valueMatrix;

    /**
     * Puissance sélectionnée comme choix final pour l'implant
     */
    private Float selectedPower;

    /**
     * Indique s'il s'agit du deuxième œil à opérer
     */
    private Boolean isSecondEye;

    /**
     * Puissance sélectionnée précédemment (si comparaison avec un autre œil)
     */
    private Float precPowerSelected;

    /**
     * Sphère équivalente (équivalent sphérique) du résultat
     */
    private Float se;

    /**
     * Référence vers un examen précédent (utile pour comparaison ou second œil)
     */
    private Long idReferencePrecExam;

    /**
     * Référence vers un calcul précédent (utile pour second œil ou duplication)
     */
    private Long idReferencePrecCalcul;
}
