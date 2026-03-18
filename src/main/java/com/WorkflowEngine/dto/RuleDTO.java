package com.WorkflowEngine.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class RuleDTO {
    private UUID id;
    private UUID stepId;
    private String condition;
    private UUID nextStepId;
    private Integer priority;
    private LocalDateTime createdAt;

    public RuleDTO() {
    }

    public RuleDTO(UUID id, UUID stepId, String condition, UUID nextStepId, Integer priority, LocalDateTime createdAt) {
        this.id = id;
        this.stepId = stepId;
        this.condition = condition;
        this.nextStepId = nextStepId;
        this.priority = priority;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStepId() {
        return stepId;
    }

    public void setStepId(UUID stepId) {
        this.stepId = stepId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public UUID getNextStepId() {
        return nextStepId;
    }

    public void setNextStepId(UUID nextStepId) {
        this.nextStepId = nextStepId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
