package com.WorkflowEngine.dto;

import java.util.Map;
import java.util.UUID;

public class WorkflowRequest {
    private String name;
    private Map<String, Object> inputSchema;
    private UUID startStepId;
    private Boolean isActive = true;

    public WorkflowRequest() {
    }

    public WorkflowRequest(String name, Map<String, Object> inputSchema, UUID startStepId, Boolean isActive) {
        this.name = name;
        this.inputSchema = inputSchema;
        this.startStepId = startStepId;
        this.isActive = isActive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
