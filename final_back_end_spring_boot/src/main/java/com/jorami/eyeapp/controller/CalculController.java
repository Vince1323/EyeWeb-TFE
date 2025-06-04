// Début du fichier completement commenté pour CalculController.java
package com.jorami.eyeapp.controller;

// DTOs utilisés pour l'échange entre le frontend et le backend
import com.jorami.eyeapp.dto.calcul.CalculDto;
import com.jorami.eyeapp.dto.calcul.CalculSummaryDto;

// Enumération des côtés de l’oeil (OD/OS)
import com.jorami.eyeapp.model.Enum;

// Service contenant la logique métier pour les calculs
import com.jorami.eyeapp.service.CalculService;

// Mapper pour convertir entre les entités Calcul et les DTOs utilisés en front
import com.jorami.eyeapp.util.mapper.CalculMapper;

// Lombok pour injecter automatiquement le constructeur avec tous les arguments
import lombok.AllArgsConstructor;

// Permet d'utiliser ResponseEntity pour gérer les réponses HTTP
import org.springframework.http.ResponseEntity;

// Permet de garantir qu'une transaction soit ouverte pendant toute l'exécution
import org.springframework.transaction.annotation.Transactional;

// Annotations Spring MVC pour construire un REST Controller
import org.springframework.web.bind.annotation.*;

// Java utils pour les listes et les maps
import java.util.List;
import java.util.Map;

// Constantes pour les messages de réponse JSON
import static com.jorami.eyeapp.exception.ConstantMessage.*;

// Autoriser les requêtes cross-origin (ex. depuis Angular)
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController // Marque cette classe comme contrôleur REST
@RequestMapping("/calculs") // Toutes les routes débutent par /calculs
@AllArgsConstructor // Constructeur injecté automatiquement avec les dépendances
@ControllerAdvice // Pour gérer les exceptions globalement dans ce contrôleur
public class CalculController {

    private final CalculService calculService; // Service lié à la logique métier
    private final CalculMapper mapper; // Conversion entité <-> DTO

    /**
     * Retourne un calcul spécifique sous forme de DTO étendu (matrice)
     */
    @GetMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<?> getCalculById(@PathVariable("id") Long id, @PathVariable("organizationsId") List<Long> organizationsId) {
        return ResponseEntity.ok(CalculMapper.convertCalculToCalculMatrixDto(mapper, calculService.getCalculById(id, organizationsId)));
    }

    /**
     * Récupérer tous les calculs pour un patient et un côté donné (OD/OS)
     */
    @GetMapping("/patients/{patientId}/organizations/{organizationsId}")
    public ResponseEntity<?> getAllCalculsByPatientId(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide) {
        List<CalculSummaryDto> calculDtos = mapper.toCalculsSummaryDto(
                calculService.getAllCalculsByPatientIdAndEyeSide(
                        patientId, Enum.EyeSide.valueOf(eyeSide), organizationsId
                ));
        return ResponseEntity.ok(calculDtos);
    }

    /**
     * Récupère les calculs précis pour un examen, un côté, et uniquement ceux ayant une puissance sélectionnée
     */
    @GetMapping("/exam/{examId}/organizations/{organizationsId}/prec/list")
    public ResponseEntity<?> getAllCalculsByExamIdEyeSidePowerTrue(@PathVariable("examId") Long examId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide) {
        List<CalculSummaryDto> calculDtos = mapper.toCalculsSummaryDto(
                calculService.getAllCalculsByExamIdEyeSidePowerTrue(
                        examId, Enum.EyeSide.valueOf(eyeSide), organizationsId
                ));
        return ResponseEntity.ok(calculDtos);
    }

    /**
     * Met à jour la valeur de la puissance sélectionnée (dioptrie) pour un calcul
     */
    @PutMapping("/organizationsId/{organizationsId}/select-power")
    public ResponseEntity<?> selectPower(@RequestParam("idCalcul") Long idCalcul, @RequestParam("power") Float power, @PathVariable("organizationsId") List<Long> organizationsId) {
        calculService.updateSelectedDiopters(idCalcul, power, organizationsId);
        return ResponseEntity.ok(Map.of(JSON_KEY, UPDATED));
    }

    /**
     * Injecte une liste de calculs dans les organisations (via le mapping front)
     */
    @PostMapping("/organizationsId/{organizationsId}")
    public ResponseEntity<?> injectionOnEachWebSite(@RequestBody List<CalculDto> calculsDto, @PathVariable("organizationsId") List<Long> organizationsId) {
        calculService.loopOnCalculs(mapper.toCalculs(calculsDto), organizationsId);
        return ResponseEntity.ok(Map.of(JSON_KEY, ADDED));
    }
}