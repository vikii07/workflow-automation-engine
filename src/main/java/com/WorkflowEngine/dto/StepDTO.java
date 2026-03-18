package com.WorkflowEngine.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StepDTO {
    private UUID id;
    private UUID workflowId;
    private String name;
    private String stepType;
    private Integer stepOrder;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private List<RuleDTO> rules;

    public StepDTO() {
    }

    public StepDTO(UUID id, UUID workflowId, String name, String stepType, Integer stepOrder,
                   Map<String, Object> metadata, LocalDateTime createdAt, List<RuleDTO> rules) {
        this.id = id;
        this.workflowId = workflowId;
        this.name = name;
        this.stepType = stepType;
        this.stepOrder = stepOrder;
        this.metadata = metadata;
        this.createdAt = createdAt;
        this.rules = rules;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(UUID workflowId) {
        this.workflowId = workflowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public void setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<RuleDTO> getRules() {
        return rules;
    }

    public void setRules(List<RuleDTO> rules) {
        this.rules = rules;
    }
}
