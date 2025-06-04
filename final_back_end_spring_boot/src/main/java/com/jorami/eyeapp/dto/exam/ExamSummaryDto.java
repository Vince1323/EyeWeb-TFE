package com.jorami.eyeapp.dto.exam;

// Importation des annotations Lombok pour générer automatiquement les getters, setters et constructeurs
import lombok.AllArgsConstructor; // Génère un constructeur avec tous les arguments
import lombok.Getter;             // Génère tous les getters
import lombok.NoArgsConstructor;  // Génère un constructeur sans argument
import lombok.Setter;             // Génère tous les setters

/**
 * ExamSummaryDto est un DTO allégé qui contient uniquement les informations essentielles d’un examen,
 * utilisé pour l’affichage rapide (ex. listes, tables de résultats). Il est plus léger que le DTO complet ExamDto.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
/**
 * DTO simplifié pour l'affichage allégé d’un examen (vue résumé).
 * Implémente BaseExamDto pour permettre le retour conditionnel typé.
 */
public class ExamSummaryDto implements BaseExamDto {
    /**
     * Identifiant unique de l’examen (clé primaire côté base de données)
     * @return ID unique
     */
    private Long id;

    /**
     * Date à laquelle un calcul de biométrie a été effectué sur cet examen.
     * Cette information permet de savoir si l’examen a déjà été exploité.
     * @return date de calcul
     */
    private String calculDate;

    /**
     * Numéro de version de l'examen (utile pour la gestion du versioning / optimistic locking)
     * @return version long
     */
    private Long version;
}
