package com.WorkflowEngine.repository;

import com.WorkflowEngine.model.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RuleRepository extends JpaRepository<Rule, UUID> {
    List<Rule> findByStepIdOrderByPriorityAsc(UUID stepId);
    void deleteByStepId(UUID stepId);
}
