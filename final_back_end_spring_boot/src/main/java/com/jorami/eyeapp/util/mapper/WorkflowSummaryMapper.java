package com.jorami.eyeapp.util.mapper;

import com.jorami.eyeapp.dto.operation.WorkflowSummaryDto;
import com.jorami.eyeapp.model.operation.WorkflowSummary;
import org.springframework.stereotype.Component;

@Component
public class WorkflowSummaryMapper {

    public WorkflowSummaryDto toDto(WorkflowSummary entity) {
        WorkflowSummaryDto dto = new WorkflowSummaryDto();
        dto.setId(entity.getId());
        dto.setWorkflowId(entity.getWorkflow().getId());
        dto.setAtcdO(entity.getAtcdO());
        dto.setMedicalHistory(entity.getMedicalHistory());
        dto.setDominantEye(entity.getDominantEye());
        dto.setAllergy(entity.getAllergy());
        dto.setAnesthesia(entity.getAnesthesia());
        dto.setSurgeryType(entity.getSurgeryType());
        dto.setDateSurgery(entity.getDateSurgery());
        dto.setLaterality(entity.getLaterality());
        dto.setSurgeon(entity.getSurgeon());
        dto.setVisitWithSurgeon(entity.getVisitWithSurgeon());
        dto.setIol(entity.getIol());
        dto.setOperatingMaterials(entity.getOperatingMaterials());
        dto.setPreopTreatments(entity.getPreopTreatments());
        dto.setPostopAppointments(entity.getPostopAppointments());
        return dto;
    }

    public WorkflowSummary toEntity(WorkflowSummaryDto dto) {
        WorkflowSummary entity = new WorkflowSummary();
        entity.setId(dto.getId());
        entity.setAtcdO(dto.getAtcdO());
        entity.setMedicalHistory(dto.getMedicalHistory());
        entity.setDominantEye(dto.getDominantEye());
        entity.setAllergy(dto.getAllergy());
        entity.setAnesthesia(dto.getAnesthesia());
        entity.setSurgeryType(dto.getSurgeryType());
        entity.setDateSurgery(dto.getDateSurgery());
        entity.setLaterality(dto.getLaterality());
        entity.setSurgeon(dto.getSurgeon());
        entity.setVisitWithSurgeon(dto.getVisitWithSurgeon());
        entity.setIol(dto.getIol());
        entity.setOperatingMaterials(dto.getOperatingMaterials());
        entity.setPreopTreatments(dto.getPreopTreatments());
        entity.setPostopAppointments(dto.getPostopAppointments());
        return entity;
    }

    public void updateEntity(WorkflowSummary entity, WorkflowSummaryDto dto) {
        entity.setAtcdO(dto.getAtcdO());
        entity.setMedicalHistory(dto.getMedicalHistory());
        entity.setDominantEye(dto.getDominantEye());
        entity.setAllergy(dto.getAllergy());
        entity.setAnesthesia(dto.getAnesthesia());
        entity.setSurgeryType(dto.getSurgeryType());
        entity.setDateSurgery(dto.getDateSurgery());
        entity.setLaterality(dto.getLaterality());
        entity.setSurgeon(dto.getSurgeon());
        entity.setVisitWithSurgeon(dto.getVisitWithSurgeon());
        entity.setIol(dto.getIol());
        entity.setOperatingMaterials(dto.getOperatingMaterials());
        entity.setPreopTreatments(dto.getPreopTreatments());
        entity.setPostopAppointments(dto.getPostopAppointments());
    }
}
