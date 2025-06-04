package com.jorami.eyeapp.service;

import com.jorami.eyeapp.model.operation.WorkflowSummary;

public interface WorkflowSummaryService {
    WorkflowSummary getByWorkflowId(Long workflowId);
    WorkflowSummary saveOrUpdate(Long workflowId, WorkflowSummary updatedData);
}
