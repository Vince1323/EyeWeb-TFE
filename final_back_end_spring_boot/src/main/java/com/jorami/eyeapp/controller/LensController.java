package com.jorami.eyeapp.controller;

// === Import des DTOs ===
// LensDto est un objet de transfert de données utilisé pour exposer les lentilles côté frontend
import com.jorami.eyeapp.dto.LensDto;

// === Import du service métier ===
// Le service gère la logique de récupération et d'importation des lentilles
import com.jorami.eyeapp.service.LensService;

// === Mapper : conversion entre entités (base de données) et DTOs ===
import com.jorami.eyeapp.util.mapper.LensMapper;

// === Lombok : génère un constructeur avec tous les arguments automatiquement ===
import lombok.AllArgsConstructor;

// === Spring ===
import org.springframework.transaction.annotation.Transactional; // Pour ouvrir une transaction pour les appels en base
import org.springframework.web.bind.annotation.*; // Pour la gestion des routes REST

import java.util.List;

/**
 * Contrôleur REST responsable des opérations liées aux lentilles.
 * Expose des endpoints accessibles via HTTP pour interagir avec le frontend Angular.
 */
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
// Permet à Spring de démarrer une transaction automatiquement pour chaque méthode du contrôleur
@Transactional
// Indique que cette classe est un contrôleur REST, les réponses seront automatiquement converties en JSON
@RestController
// Définit le préfixe commun pour toutes les routes : /lenses
@RequestMapping("/lenses")
// Lombok génère un constructeur avec tous les attributs finaux automatiquement
@AllArgsConstructor
public class LensController {

    // Service qui contient la logique métier (accès DB, traitements, etc.)
    private final LensService lensService;

    // Mapper pour convertir les entités en DTOs (évite d’exposer l’entité JPA directement)
    private final LensMapper lensMapper;

    /**
     * Endpoint HTTP GET : permet de récupérer toutes les lentilles associées à un fabricant donné.
     * @param lensManufacturerId identifiant du fabricant de lentilles
     * @return une liste de lentilles au format DTO (JSON)
     */
    @GetMapping("/manufacturers/{id}")
    public List<LensDto> getLensesByManufacturerId(@PathVariable("id") Long lensManufacturerId) {
        return lensMapper.toLensDtos(lensService.getLensesByLensManufacturer(lensManufacturerId));
    }

    /**
     * Endpoint HTTP POST : déclenche un import de lentilles depuis une source externe (ex: site web d’un fabricant).
     * Ce traitement est côté backend uniquement (aucun paramètre nécessaire).
     */
    @PostMapping
    public void importLensesFromWebSite() {
        lensService.importLensesFromWebSite();
    }
}
