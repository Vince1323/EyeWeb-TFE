package com.jorami.eyeapp.controller;

// === Service métier : gère la logique liée aux fabricants de lentilles ===
import com.jorami.eyeapp.service.LensManufacturerService;

// === Mapper : convertit les entités en DTOs adaptés pour le frontend ===
import com.jorami.eyeapp.util.mapper.LensManufacturerMapper;

// === Lombok : génère un constructeur avec tous les arguments pour les propriétés final ===
import lombok.AllArgsConstructor;

// === Spring Framework : gestion HTTP, transactions et annotations REST ===
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * Contrôleur REST responsable de la gestion des fabricants de lentilles.
 * Expose une route "/lens-manufacturers" pour récupérer tous les fabricants existants.
 */
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"}, // Autorise le frontend Angular ou l'app déployée à interagir avec le backend
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, // Méthodes autorisées
        maxAge = 3600L // Durée pendant laquelle la pré-vérification CORS peut être mise en cache par le navigateur
)
// Démarre une transaction automatiquement pour chaque méthode de ce contrôleur
@Transactional
// Déclare un contrôleur REST : toutes les méthodes retournent directement du JSON
@RestController
// Définit le préfixe commun des routes : /lens-manufacturers
@RequestMapping("/lens-manufacturers")
// Lombok : génère automatiquement un constructeur avec les dépendances
@AllArgsConstructor
// Permet d'intercepter et de gérer les erreurs de manière globale si besoin
@ControllerAdvice
public class LensManufacturerController {

    // Service métier pour accéder aux données des fabricants
    private final LensManufacturerService lensManufacturer;

    // Mapper pour convertir les entités JPA vers des DTOs
    private final LensManufacturerMapper mapper;

    /**
     * Endpoint HTTP GET pour récupérer la liste de tous les fabricants de lentilles présents en base.
     * @return une liste de DTOs représentant les fabricants (sous forme de JSON)
     */
    @GetMapping
    public ResponseEntity<?> getAllLensManufacturers() {
        // Appelle le service pour récupérer les entités, puis les convertit en DTOs pour le frontend
        return ResponseEntity.ok().body(mapper.toLensManufacturerDtos(lensManufacturer.getAllLensManufacturers()));
    }

}
