package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.Workflow;
import com.jorami.eyeapp.model.operation.WorkflowSummary;
import com.jorami.eyeapp.repository.WorkflowSummaryRepository;
import com.jorami.eyeapp.repository.WorkflowRepository;
import com.jorami.eyeapp.service.WorkflowSummaryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkflowSummaryServiceImpl implements WorkflowSummaryService {

    private final WorkflowSummaryRepository summaryRepo;
    private final WorkflowRepository workflowRepo;

    @Override
    public WorkflowSummary getByWorkflowId(Long workflowId) {
        return summaryRepo.findByWorkflowId(workflowId)
                .orElseThrow(() -> new EntityNotFoundException("Summary not found for workflow " + workflowId));
    }

    @Override
    public WorkflowSummary saveOrUpdate(Long workflowId, WorkflowSummary updatedData) {
        Workflow workflow = workflowRepo.findById(workflowId)
                .orElseThrow(() -> new EntityNotFoundException("Workflow not found with id " + workflowId));

        WorkflowSummary existing = summaryRepo.findByWorkflowId(workflowId).orElse(null);
        if (existing != null) {
            updatedData.setId(existing.getId());
        }

        updatedData.setWorkflow(workflow);
        return summaryRepo.save(updatedData);
    }
}
