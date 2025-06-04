package com.jorami.eyeapp.repository;

import com.jorami.eyeapp.model.operation.WorkflowSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkflowSummaryRepository extends JpaRepository<WorkflowSummary, Long> {
    Optional<WorkflowSummary> findByWorkflowId(Long workflowId);
}
