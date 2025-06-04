package com.jorami.eyeapp.dto.calcul;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Ce DTO (Data Transfer Object) sert à exposer une version résumée d’un calcul,
 * généralement utilisée pour les listes ou les aperçus.
 * Il contient uniquement les métadonnées essentielles (id, date de création, version).
 */
@NoArgsConstructor // Génère un constructeur vide (utile pour la désérialisation)
@AllArgsConstructor // Génère un constructeur avec tous les attributs
@Getter // Génère les getters automatiquement
@Setter // Génère les setters automatiquement
public class CalculSummaryDto {

    /**
     * Identifiant unique du calcul
     *
     * @return l'id du calcul
     */
    private long id;

    /**
     * Date et heure de création du calcul (pour tri, affichage, etc.)
     *
     * @return la date de création
     */
    private LocalDateTime createdAt;

    /**
     * Numéro de version du calcul (permet de suivre l’évolution ou les recalculs)
     *
     * @return le numéro de version
     */
    private long version;
}
