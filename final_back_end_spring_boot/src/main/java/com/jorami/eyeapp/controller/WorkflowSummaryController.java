package com.jorami.eyeapp.controller;

// Import du DTO représentant les données transférées entre back et front pour le résumé opératoire
import com.jorami.eyeapp.dto.operation.WorkflowSummaryDto;

// Import de l'entité métier utilisée en base de données
import com.jorami.eyeapp.model.operation.WorkflowSummary;

// Service métier responsable de la gestion des résumés opératoires
import com.jorami.eyeapp.service.WorkflowSummaryService;

// Mapper pour convertir entre l'entité et le DTO
import com.jorami.eyeapp.util.mapper.WorkflowSummaryMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST permettant de gérer les résumés opératoires d’un workflow.
 * Tous les endpoints sont accessibles via le préfixe "/summary".
 */
@CrossOrigin(
        origins = {"http://localhost:4200/", "https://eyewebapp.com/"},
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        maxAge = 3600L
)
@RestController
@RequestMapping("/summary")
@RequiredArgsConstructor
@Transactional
public class WorkflowSummaryController {

    // Service métier pour gérer les résumés opératoires
    private final WorkflowSummaryService summaryService;

    // Mapper pour convertir entre entité et DTO
    private final WorkflowSummaryMapper mapper;

    /**
     * Endpoint GET pour récupérer le résumé opératoire d’un workflow donné.
     *
     * @param workflowId Identifiant du workflow dont on veut le résumé
     * @return Résumé au format DTO dans un ResponseEntity
     */
    @GetMapping("/{workflowId}")
    public ResponseEntity<WorkflowSummaryDto> getSummary(@PathVariable Long workflowId) {
        WorkflowSummary summary = summaryService.getByWorkflowId(workflowId);
        return ResponseEntity.ok(mapper.toDto(summary));
    }

    /**
     * Endpoint PUT pour modifier le résumé opératoire d’un workflow donné.
     *
     * @param workflowId Identifiant du workflow à modifier
     * @param dto Données modifiées du résumé opératoire au format DTO
     * @return Résumé mis à jour au format DTO dans un ResponseEntity
     */
    @PutMapping("/{workflowId}")
    public ResponseEntity<WorkflowSummaryDto> updateSummary(@PathVariable Long workflowId, @RequestBody WorkflowSummaryDto dto) {
        WorkflowSummary updated = new WorkflowSummary();

        // Mise à jour manuelle des champs de l'entité à partir du DTO
        mapper.updateEntity(updated, dto);

        // Sauvegarde ou mise à jour via le service
        WorkflowSummary saved = summaryService.saveOrUpdate(workflowId, updated);

        // Conversion en DTO et retour au front
        return ResponseEntity.ok(mapper.toDto(saved));
    }
}
