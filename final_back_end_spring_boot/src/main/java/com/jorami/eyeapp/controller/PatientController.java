package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.patient.PatientDto;
import com.jorami.eyeapp.dto.patient.PatientSummaryDto;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.service.PatientService;
import com.jorami.eyeapp.util.mapper.PatientMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST permettant de gérer les patients dans le système.
 * Tous les endpoints de cette classe sont préfixés par /patients.
 */
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"}, // Autorise le front-end à accéder à ces endpoints
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}, // Méthodes HTTP autorisées
        maxAge = 3600L // Durée pendant laquelle la réponse CORS peut être mise en cache (1h)
)
@Transactional // Chaque requête est exécutée dans une transaction. Rollback automatique en cas d’erreur.
@RestController // Indique qu'il s'agit d'un contrôleur REST (équivalent à @Controller + @ResponseBody)
@RequestMapping("/patients") // Préfixe global pour tous les endpoints définis dans ce contrôleur
@AllArgsConstructor // Lombok : génère un constructeur avec tous les champs requis
@ControllerAdvice // Permet de centraliser le traitement des erreurs (utilisable avec @ExceptionHandler)
public class PatientController {

    private final PatientService patientService; // Service métier pour la logique de gestion des patients
    private final PatientMapper mapper; // Mapper DTO <-> Entité

    /**
     * Récupère tous les patients associés à une ou plusieurs organisations.
     *
     * @param organizationsId liste des IDs d'organisations auxquelles les patients doivent appartenir
     * @return ResponseEntity contenant la liste des patients résumés (PatientSummaryDto)
     */
    @GetMapping("/organizations/{organizationsId}")
    public ResponseEntity<List<PatientSummaryDto>> getAllPatientsByOrganizationId(@PathVariable("organizationsId") List<Long> organizationsId) {
        List<PatientSummaryDto> patientDtos = mapper.toPatientSummaryDtos(
                patientService.getAllPatientsSummariesByOrganizationsId(organizationsId)
        );
        return ResponseEntity.ok(patientDtos);
    }

    /**
     * Récupère un patient spécifique via son ID, en vérifiant qu’il appartient à une des organisations spécifiées.
     *
     * @param patientId l'identifiant unique du patient
     * @param organizationsId liste d’IDs d’organisations autorisées
     * @return DTO du patient s’il existe et appartient à une organisation autorisée
     */
    @GetMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<PatientDto> getPatientById(
            @PathVariable("id") Long patientId,
            @PathVariable("organizationsId") List<Long> organizationsId) {
        Patient patient = patientService.getPatientById(organizationsId, patientId);
        PatientDto patientDto = mapper.toPatientDto(patient);
        return ResponseEntity.ok(patientDto);
    }

    /**
     * Ajoute un nouveau patient dans la base de données, en l'associant à une ou plusieurs organisations.
     *
     * @param organizationsId liste des IDs d'organisations auxquelles le patient sera associé
     * @param patientDto les données du patient à créer (validées automatiquement)
     * @return le PatientDto correspondant au patient créé
     */
    @PostMapping("/organizations/{organizationsId}")
    public ResponseEntity<PatientDto> addPatient(
            @PathVariable("organizationsId") List<Long> organizationsId,
            @Valid @RequestBody PatientDto patientDto) {
        Patient newPatient = patientService.addPatient(mapper.toPatient(patientDto), organizationsId);
        return ResponseEntity.ok(mapper.toPatientDto(newPatient));
    }

    /**
     * Modifie les informations d’un patient existant.
     *
     * @param patientId identifiant du patient à modifier
     * @param organizationsId liste des organisations pour la validation
     * @param patientDto les nouvelles données du patient
     * @return DTO mis à jour ou erreur 500 en cas d'échec
     */
    @PutMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<PatientDto> editPatient(
            @PathVariable("id") Long patientId,
            @PathVariable("organizationsId") List<Long> organizationsId,
            @Valid @RequestBody PatientDto patientDto) {

        Patient updatedPatient = patientService.editPatient(patientId, mapper.toPatient(patientDto), organizationsId);
        if (updatedPatient == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(mapper.toPatientDto(updatedPatient));
    }

    /**
     * Supprime un patient (logiquement ou physiquement selon l’implémentation du service).
     *
     * @param patientId identifiant du patient à supprimer
     * @param organizationsId liste des organisations autorisées
     * @return le PatientDto du patient supprimé
     */
    @PutMapping("/{id}/organizations/{organizationsId}/delete")
    public ResponseEntity<PatientDto> deletePatient(
            @PathVariable("id") Long patientId,
            @PathVariable("organizationsId") List<Long> organizationsId) {
        Patient deletedPatient = patientService.deletePatient(patientId, organizationsId);
        return ResponseEntity.ok(mapper.toPatientDto(deletedPatient));
    }

    @GetMapping("/organizations/{organizationsId}/filter")
    public ResponseEntity<Page<PatientSummaryDto>> filterPatients(
            @PathVariable("organizationsId") List<Long> organizationsId,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Patient> patientsPage = patientService.getFilteredPatients(organizationsId, search, page, size);
        Page<PatientSummaryDto> dtoPage = patientsPage.map(mapper::toPatientSummaryDto);
        return ResponseEntity.ok(dtoPage);
    }

}
