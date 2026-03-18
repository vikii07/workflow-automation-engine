package com.WorkflowEngine.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkflowDTO {
    private UUID id;
    private String name;
    private Integer version;
    private Boolean isActive;
    private Map<String, Object> inputSchema;
    private UUID startStepId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int stepCount;
    private List<StepDTO> steps;

    public WorkflowDTO() {
    }

    public WorkflowDTO(UUID id, String name, Integer version, Boolean isActive, Map<String, Object> inputSchema,
                       UUID startStepId, LocalDateTime createdAt, LocalDateTime updatedAt,
                       int stepCount, List<StepDTO> steps) {
        this.id = id;
        this.name = name;
        this.version = version;
        this.isActive = isActive;
        this.inputSchema = inputSchema;
        this.startStepId = startStepId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stepCount = stepCount;
        this.steps = steps;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Map<String, Object> getInputSchema() {
        return inputSchema;
    }

    public void setInputSchema(Map<String, Object> inputSchema) {
        this.inputSchema = inputSchema;
    }

    public UUID getStartStepId() {
        return startStepId;
    }

    public void setStartStepId(UUID startStepId) {
        this.startStepId = startStepId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public List<StepDTO> getSteps() {
        return steps;
    }

    public void setSteps(List<StepDTO> steps) {
        this.steps = steps;
    }
}
