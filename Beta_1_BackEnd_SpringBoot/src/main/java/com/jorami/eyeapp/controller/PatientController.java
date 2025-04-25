package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.patient.PatientDto;
import com.jorami.eyeapp.dto.patient.PatientSummaryDto;
import com.jorami.eyeapp.model.patient.Patient;
import com.jorami.eyeapp.service.PatientService;
import com.jorami.eyeapp.util.mapper.PatientMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@Transactional
@RestController
@RequestMapping("/patients")
@AllArgsConstructor
@ControllerAdvice
public class PatientController {

    private final PatientService patientService;
    private final PatientMapper mapper;

    /** Lire tous les patients d'une organisation */
    @GetMapping("/organizations/{organizationsId}")
    public ResponseEntity<List<PatientSummaryDto>> getAllPatientsByOrganizationId(@PathVariable("organizationsId") List<Long> organizationsId) {
        List<PatientSummaryDto> patientDtos = mapper.toPatientSummaryDtos(patientService.getAllPatientsSummariesByOrganizationsId(organizationsId));
        return ResponseEntity.ok(patientDtos);
    }

    /** Lire un patient spécifique */
    @GetMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable("id") Long patientId, @PathVariable("organizationsId") List<Long> organizationsId) {
        Patient patient = patientService.getPatientById(organizationsId, patientId);
        PatientDto patientDto = mapper.toPatientDto(patient);
        return ResponseEntity.ok(patientDto);
    }

    /** Créer un patient */
    @PostMapping("/organizations/{organizationsId}")
    public ResponseEntity<PatientDto> addPatient(@PathVariable("organizationsId") List<Long> organizationsId, @Valid @RequestBody PatientDto patientDto) {
        return ResponseEntity.ok(mapper.toPatientDto(patientService.addPatient(mapper.toPatient(patientDto), organizationsId)));
    }

    /** Modifier un patient */
    @PutMapping("/{id}/organizations/{organizationsId}")
    public ResponseEntity<PatientDto> editPatient(
            @PathVariable("id") Long patientId,
            @PathVariable("organizationsId") List<Long> organizationsId,
            @Valid @RequestBody PatientDto patientDto) {
        Patient updatedPatient = patientService.editPatient(patientId, mapper.toPatient(patientDto), organizationsId);
           if (updatedPatient == null) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        PatientDto updatedPatientDto = mapper.toPatientDto(updatedPatient);
        return ResponseEntity.ok(updatedPatientDto);
    }

    /** Supprimer un patient */
    @PutMapping("/{id}/organizations/{organizationsId}/delete")
    public ResponseEntity<PatientDto> deletePatient(
            @PathVariable("id") Long patientId,
            @PathVariable("organizationsId") List<Long> organizationsId) {
        Patient deletedPatient = patientService.deletePatient(patientId, organizationsId);
        return ResponseEntity.ok(mapper.toPatientDto(deletedPatient));
    }

}


