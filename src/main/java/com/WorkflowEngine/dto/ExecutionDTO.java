package com.WorkflowEngine.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExecutionDTO {
    private UUID id;
    private UUID workflowId;
    private String workflowName;
    private Integer workflowVersion;
    private String status;
    private Map<String, Object> data;
    private UUID currentStepId;
    private String currentStepName;
    private Integer retries;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private List<ExecutionLogDTO> logs;

    public ExecutionDTO() {
    }

    public ExecutionDTO(UUID id, UUID workflowId, String workflowName, Integer workflowVersion, String status,
                        Map<String, Object> data, UUID currentStepId, String currentStepName, Integer retries,
                        LocalDateTime startedAt, LocalDateTime endedAt, List<ExecutionLogDTO> logs) {
        this.id = id;
        this.workflowId = workflowId;
        this.workflowName = workflowName;
        this.workflowVersion = workflowVersion;
        this.status = status;
        this.data = data;
        this.currentStepId = currentStepId;
        this.currentStepName = currentStepName;
        this.retries = retries;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.logs = logs;
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

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public Integer getWorkflowVersion() {
        return workflowVersion;
    }

    public void setWorkflowVersion(Integer workflowVersion) {
        this.workflowVersion = workflowVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public UUID getCurrentStepId() {
        return currentStepId;
    }

    public void setCurrentStepId(UUID currentStepId) {
        this.currentStepId = currentStepId;
    }

    public String getCurrentStepName() {
        return currentStepName;
    }

    public void setCurrentStepName(String currentStepName) {
        this.currentStepName = currentStepName;
    }

    public Integer getRetries() {
        return retries;
    }

    public void setRetries(Integer retries) {
        this.retries = retries;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public List<ExecutionLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<ExecutionLogDTO> logs) {
        this.logs = logs;
    }
}
