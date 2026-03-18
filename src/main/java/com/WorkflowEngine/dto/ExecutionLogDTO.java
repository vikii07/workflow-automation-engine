package com.WorkflowEngine.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExecutionLogDTO {
    private UUID id;
    private UUID executionId;
    private String stepName;
    private String stepType;
    private List<Map<String, Object>> evaluatedRules;
    private UUID selectedNextStep;
    private String selectedNextStepName;
    private String status;
    private UUID approverId;
    private String errorMessage;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private long durationMs;

    public ExecutionLogDTO() {
    }

    public ExecutionLogDTO(UUID id, UUID executionId, String stepName, String stepType,
                           List<Map<String, Object>> evaluatedRules, UUID selectedNextStep,
                           String selectedNextStepName, String status, UUID approverId,
                           String errorMessage, LocalDateTime startedAt, LocalDateTime endedAt,
                           long durationMs) {
        this.id = id;
        this.executionId = executionId;
        this.stepName = stepName;
        this.stepType = stepType;
        this.evaluatedRules = evaluatedRules;
        this.selectedNextStep = selectedNextStep;
        this.selectedNextStepName = selectedNextStepName;
        this.status = status;
        this.approverId = approverId;
        this.errorMessage = errorMessage;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.durationMs = durationMs;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExecutionId() {
        return executionId;
    }

    public void setExecutionId(UUID executionId) {
        this.executionId = executionId;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public List<Map<String, Object>> getEvaluatedRules() {
        return evaluatedRules;
    }

    public void setEvaluatedRules(List<Map<String, Object>> evaluatedRules) {
        this.evaluatedRules = evaluatedRules;
    }

    public UUID getSelectedNextStep() {
        return selectedNextStep;
    }

    public void setSelectedNextStep(UUID selectedNextStep) {
        this.selectedNextStep = selectedNextStep;
    }

    public String getSelectedNextStepName() {
        return selectedNextStepName;
    }

    public void setSelectedNextStepName(String selectedNextStepName) {
        this.selectedNextStepName = selectedNextStepName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getApproverId() {
        return approverId;
    }

    public void setApproverId(UUID approverId) {
        this.approverId = approverId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }
}
