package com.WorkflowEngine.repository;

import com.WorkflowEngine.model.Workflow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WorkflowRepository extends JpaRepository<Workflow, UUID> {

    Page<Workflow> findByIsActiveTrue(Pageable pageable);

    Page<Workflow> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Workflow> findByIsActive(Boolean isActive, Pageable pageable);

    Page<Workflow> findByNameContainingIgnoreCaseAndIsActive(String name, Boolean isActive, Pageable pageable);
}
