package com.WorkflowEngine.repository;

import com.WorkflowEngine.model.Execution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExecutionRepository extends JpaRepository<Execution, UUID> {

    Page<Execution> findByIsArchivedFalse(Pageable pageable);

    Page<Execution> findByIsArchivedTrue(Pageable pageable);

    Page<Execution> findByWorkflowIdAndIsArchivedFalse(UUID workflowId, Pageable pageable);
}
