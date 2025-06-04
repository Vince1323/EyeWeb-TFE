package com.jorami.eyeapp.controller;

import com.jorami.eyeapp.dto.exam.ExamDto;
import com.jorami.eyeapp.model.exam.Exam;
import com.jorami.eyeapp.service.CataractService;
import com.jorami.eyeapp.util.mapper.ExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jorami.eyeapp.model.Enum;


import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final CataractService cataractService;
    private final ExamMapper examMapper;

    /**
     * Endpoint pour récupérer un examen par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExamDto> getExamById(@PathVariable Long id, @RequestParam List<Long> orgaIds) {
        Exam exam = cataractService.getExamById(id, orgaIds);
        return ResponseEntity.ok(examMapper.toExamDto(exam));
    }

    /**
     * Endpoint pour récupérer tous les examens d’un patient selon l’œil concerné.
     */
    @GetMapping("/by-patient/{patientId}")
    public ResponseEntity<List<ExamDto>> getExamsByPatient(
            @PathVariable Long patientId,
            @RequestParam List<Long> orgaIds,
            @RequestParam(required = false) String eyeSide,
            @RequestParam(defaultValue = "false") boolean selectedOnly) {

        List<Exam> exams = cataractService.getAllBiometriesByPatientIdAndEyeSide(
                patientId,
                eyeSide != null ? Enum.EyeSide.valueOf(eyeSide.toUpperCase()) : null,
                selectedOnly,
                orgaIds
        );

        return ResponseEntity.ok(examMapper.toExamDtos(exams));
    }

    /**
     * Endpoint pour ajouter un nouvel examen à un patient donné.
     */
    @PostMapping("/patient/{patientId}")
    public ResponseEntity<ExamDto> createExam(
            @PathVariable Long patientId,
            @RequestParam List<Long> orgaIds,
            @RequestBody ExamDto examDto) {

        Exam exam = examMapper.toExam(examDto);
        Exam savedExam = cataractService.addExam(exam, patientId, orgaIds);
        return ResponseEntity.ok(examMapper.toExamDto(savedExam));
    }

    /**
     * (Optionnel) Endpoint pour mettre à jour un examen existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExamDto> updateExam(
            @PathVariable Long id,
            @RequestParam List<Long> orgaIds,
            @RequestBody ExamDto examDto) {

        Exam exam = examMapper.toExam(examDto);
        exam.setId(id);
        Exam updatedExam = cataractService.editExam(exam, orgaIds);
        return ResponseEntity.ok(examMapper.toExamDto(updatedExam));
    }

    /**
     * Endpoint pour supprimer un examen.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long id, @RequestParam List<Long> orgaIds) {
        cataractService.deleteExam(id, orgaIds);
        return ResponseEntity.noContent().build();
    }

    /**
     * Endpoint pour récupérer les examens d’un patient avec possibilité de retour allégé (ExamSummaryDto).
     */
    @GetMapping("/by-patient-summary/{patientId}")
    public ResponseEntity<List<?>> getAllExamsByPatientId(
            @PathVariable Long patientId,
            @RequestParam List<Long> orgaIds,
            @RequestParam(required = false) String eyeSide,
            @RequestParam(defaultValue = "false") boolean isLight) {

        // Appelle le service en précisant si on veut une version "light" ou "complète"
        List<?> exams = cataractService.getAllExamsByPatientId(
                patientId,
                orgaIds,
                eyeSide,
                isLight
        );

        return ResponseEntity.ok(exams);
    }

}
