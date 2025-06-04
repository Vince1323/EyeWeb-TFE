// Package contenant tous les contrôleurs REST de l'application
package com.jorami.eyeapp.controller;

// Importation du service métier lié aux organisations (logique applicative)
import com.jorami.eyeapp.service.OrganizationService;

// Mapper qui convertit les entités Organization en DTO (Data Transfer Object)
import com.jorami.eyeapp.util.mapper.OrganizationMapper;

// Annotation Lombok : génère automatiquement un constructeur avec tous les champs requis
import lombok.AllArgsConstructor;

// Permet de construire des réponses HTTP de manière souple (code, corps, headers, etc.)
import org.springframework.http.ResponseEntity;

// Permet de gérer automatiquement les transactions sur toutes les méthodes du contrôleur
import org.springframework.transaction.annotation.Transactional;

// Annotations REST de Spring
import org.springframework.web.bind.annotation.*;


// Active les appels CORS (Cross-Origin Resource Sharing) pour l’accès depuis Angular par exemple
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)

// Assure que toute la classe est exécutée dans une transaction DB automatique (rollback en cas d'erreur)
@Transactional

// Spécifie que cette classe est un contrôleur REST (toutes les méthodes retournent des JSON/objets)
@RestController

// Définit le prefixe d’URL de tous les endpoints de ce contrôleur : /organizations
@RequestMapping("/organizations")

// Génère le constructeur avec injection de `organizationService` et `mapper`
@AllArgsConstructor
public class OrganizationController {

    // Service métier injecté automatiquement : contient la logique pour manipuler les organisations
    private final OrganizationService organizationService;

    // Mapper qui convertit les entités Organization en objets DTO (pour éviter d’exposer l'entité brute)
    private final OrganizationMapper mapper;

    /**
     * Endpoint GET /organizations/users/{id}
     * Permet de récupérer toutes les organisations associées à un utilisateur donné.
     * Ex : Un utilisateur peut appartenir à plusieurs hôpitaux, cabinets, etc.
     *
     * @param id ID de l'utilisateur
     * @return une liste de DTOs représentant les organisations
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getOrganizationsByUserId(@PathVariable("id") Long id) {
        // Appelle le service pour récupérer les organisations de l'utilisateur
        return ResponseEntity.ok(mapper.toOrganizationDtos(organizationService.getOrganizationsByUserId(id)));
    }

    /*
     * ⚠️  permet d’ajouter une organisation à un utilisateur.
     * Il vérifie que les paramètres sont valides, puis appelle le service métier pour effectuer l'ajout.
     *
     * Il utilise un `Mapper.convertTo(...)` générique (à adapter selon les DTO utilisés).
     *
     * Il aurait pu servir dans un contexte multi-organisation (ajout dynamique).
     */
    /*
    @PostMapping("/users/{id}/organizations/{organizationsId}")
    public ResponseEntity<?> addOrganization(@PathVariable("id") Long userId, @PathVariable("organizationsId") List<Long> organizationsId) {
        if(userId != null && !organizationsId.isEmpty()) {
            return ResponseEntity.ok(Mapper.convertTo(organizationService.addOrganization(userId, organizationsId), UserOrganizationRoleDto.class));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(JSON_KEY, ITEM_NOT_FOUND));
        }
    }
     */
}
