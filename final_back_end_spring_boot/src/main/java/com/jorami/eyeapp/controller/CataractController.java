package com.jorami.eyeapp.controller;

// DTOs utilisés pour les examens (biométrie, etc.)
import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.dto.exam.ExamSummaryDto;

// Service métier pour gérer les opérations liées à la cataracte
import com.jorami.eyeapp.service.CataractService;

// Mapper utilisé pour convertir les entités Exam en DTOs
import com.jorami.eyeapp.util.mapper.ExamMapper;

// Validation de données
import jakarta.validation.Valid;

// Pour l'injection via constructeur avec Lombok
import lombok.AllArgsConstructor;

// Enumération contenant notamment EyeSide (OD/OS)
import com.jorami.eyeapp.model.Enum;

// Gestion des réponses HTTP
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

// Pour la gestion des transactions JPA
import org.springframework.transaction.annotation.Transactional;

// Annotations REST de Spring
import org.springframework.web.bind.annotation.*;

// Upload de fichiers pour OCR ou import
import org.springframework.web.multipart.MultipartFile;

// Utilisation de flux réactifs pour les logs
import reactor.core.publisher.Flux;

// Utilitaires Java
import java.io.IOException;
import java.util.List;
import java.util.Map;

// Constantes de message (réponses JSON standardisées)
import static com.jorami.eyeapp.exception.ConstantMessage.*;

// Permet les appels CORS pour le frontend Angular
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
// Ouvre une transaction JPA pour toutes les méthodes de ce contrôleur
@Transactional
// Définit cette classe comme contrôleur REST
@RestController
// Préfixe commun pour tous les endpoints de cette classe
@RequestMapping("/cataract")
// Génère automatiquement un constructeur avec les dépendances requises
@AllArgsConstructor
// Pour globaliser la gestion des exceptions si nécessaire
@ControllerAdvice
public class CataractController {

    private final CataractService cataractService;
    private final ExamMapper mapper;

    /**
     * Récupère un examen précis par ID et liste d'organisations autorisées
     */
    @GetMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<?> getExamById(@PathVariable("id") Long examId, @PathVariable("organizationsId") List<Long> organizationsId) {
        ExamDto biometryDto = mapper.toExamDto(cataractService.getExamById(examId, organizationsId));
        return ResponseEntity.ok(biometryDto);
    }

    /**
     * Récupère tous les examens d'un patient, avec possibilité de filtrer par oeil et sélection
     */
    @GetMapping("/patients/{patientId}/organizations/{organizationsId}")
    public ResponseEntity<?> getAllExamsByPatientId(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide, @RequestParam(name = "isSelected", required = false) Boolean isSelected) {
        List<ExamDto> biometryDtos = mapper.toExamDtos(cataractService.getAllBiometriesByPatientIdAndEyeSide(patientId, Enum.EyeSide.valueOf(eyeSide), isSelected, organizationsId));
        return ResponseEntity.ok(biometryDtos);
    }

    /**
     * Récupère tous les examens d'un patient sous forme de liste résumée
     */
    @GetMapping("/patients/{patientId}/organizations/{organizationsId}/lists")
    public ResponseEntity<?> getAllExamsListByPatientId(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide, @RequestParam(name = "isSelected", required = false) Boolean isSelected) {
        List<ExamSummaryDto> biometryDtos = mapper.toExamSummaryDtos(cataractService.getAllBiometriesByPatientIdAndEyeSide(patientId, Enum.EyeSide.valueOf(eyeSide), isSelected, organizationsId));
        return ResponseEntity.ok(biometryDtos);
    }

    /**
     * Récupère les examens filtrés par patient, oeil et calculs valides
     */
    @GetMapping("/patients/{patientId}/organizations/{organizationsId}/prec/lists")
    public ResponseEntity<?> getAllBiometriesByPatientIdEyeSideCalculPowerTrue(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId, @RequestParam("eyeSide") String eyeSide, @RequestParam(name = "isSelected", required = false) Boolean isSelected) {
        List<ExamSummaryDto> biometryDtos = mapper.toExamSummaryDtos(cataractService.getAllBiometriesByPatientIdEyeSideCalculPowerTrue(patientId, Enum.EyeSide.valueOf(eyeSide), isSelected, organizationsId));
        return ResponseEntity.ok(biometryDtos);
    }

    /**
     * Diffuse les logs d'import en temps réel (SSE)
     */
    @GetMapping(value = "/import/logs", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamLogs(@RequestHeader HttpHeaders headers) {
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        return cataractService.streamLogs(authorizationHeader);
    }

    /**
     * Ajoute un nouvel examen avec vérification d'existence
     */
    @PostMapping("/organizations/{organizationsId}")
    public ResponseEntity<?> addExam(@Valid @RequestBody ExamDto examDto, @PathVariable("organizationsId") List<Long> organizationsId) {
        cataractService.checkIfExist(mapper.toExam(examDto), examDto.getPatientId(), organizationsId);
        ExamDto biometryAddDto = mapper.toExamDto(cataractService.addExam(mapper.toExam(examDto), examDto.getPatientId(), organizationsId));
        return ResponseEntity.ok(biometryAddDto);
    }

    /**
     * Ajoute une liste d'examens pour un patient donné
     */
    @PostMapping("/add/{patientId}/organizations/{organizationsId}")
    public ResponseEntity<?> addExams(@Valid @RequestBody List<ExamDto> examDtos,
                                      @PathVariable("patientId") Long patientId,
                                      @PathVariable("organizationsId") List<Long> organizationsId) {
        try {
            cataractService.loopAddExams(mapper.toExams(examDtos), patientId, organizationsId);
            return ResponseEntity.accepted().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erreur lors de l'ajout des examens : " + e.getMessage());
        }
    }

    /**
     * Importe des examens depuis un fichier (OCR)
     */
    @PostMapping("/imports")
    public ResponseEntity<?> importBiometries(@RequestHeader HttpHeaders headers, @RequestParam("file") MultipartFile file, @RequestParam("biometer") String biometer, @RequestParam("organizationsId") List<Long> organizationsId) throws IOException, InterruptedException {
        String authorizationHeader = headers.getFirst(HttpHeaders.AUTHORIZATION);
        cataractService.importBiometries(authorizationHeader, file, biometer, organizationsId);
        return ResponseEntity.accepted().build();
    }

    /**
     * Calcule la moyenne des examens d'un patient
     */
    @PostMapping("/patients/{patientId}/organizations/{organizationsId}/average")
    public ResponseEntity<?> calculAverage(@PathVariable("patientId") Long patientId, @PathVariable("organizationsId") List<Long> organizationId, @Valid @RequestBody List<ExamDto> examsDto) {
        ExamDto biometryDto = mapper.toExamDto(cataractService.calculAverage(patientId, mapper.toExams(examsDto), organizationId));
        return ResponseEntity.ok(biometryDto);
    }

    /**
     * Met à jour un examen existant
     */
    @PutMapping("/organizations/{organizationsId}/update")
    public ResponseEntity<?> editExam(@Valid @RequestBody ExamDto examDto, @PathVariable("organizationsId") List<Long> orgnizationsId) {
        ExamDto editedExam = mapper.toExamDto(cataractService.editExam(mapper.toExam(examDto), orgnizationsId));
        return ResponseEntity.ok(editedExam);
    }

    /**
     * Modifie le statut de sélection d’un examen
     */
    @PutMapping("/organizations/{organizationsId}/select")
    public ResponseEntity<?> editSelectExam(@Valid @RequestBody ExamDto examDto, @PathVariable("organizationsId") List<Long> orgnizationsId) {
        ExamDto editedExam = mapper.toExamDto(cataractService.editSelectedExam(mapper.toExam(examDto), orgnizationsId));
        return ResponseEntity.ok(editedExam);
    }

    /**
     * Supprime un examen par ID
     */
    @DeleteMapping("/{id}/organizations/{organizationsId}/delete")
    public ResponseEntity<?> deleteExam(@PathVariable("id") Long examId, @PathVariable("organizationsId") List<Long> orgnizationsId) {
        cataractService.deleteExam(examId, orgnizationsId);
        return ResponseEntity.ok(Map.of(JSON_KEY, DELETE_OK));
    }
}
