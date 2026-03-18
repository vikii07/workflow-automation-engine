package com.WorkflowEngine.repository;

import com.WorkflowEngine.model.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StepRepository extends JpaRepository<Step, UUID> {
    List<Step> findByWorkflowIdOrderByStepOrderAsc(UUID workflowId);
    void deleteByWorkflowId(UUID workflowId);
}
