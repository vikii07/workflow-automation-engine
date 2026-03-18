package com.WorkflowEngine.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "execution_logs")
public class ExecutionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "execution_id", nullable = false)
    private Execution execution;

    @Column(name = "step_name", length = 255)
    private String stepName;

    @Column(name = "step_type", length = 50)
    private String stepType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "evaluated_rules", columnDefinition = "jsonb")
    private List<Map<String, Object>> evaluatedRules;

    @Column(name = "selected_next_step")
    private UUID selectedNextStep;

    @Column(length = 50)
    private String status;

    @Column(name = "approver_id")
    private UUID approverId;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    public ExecutionLog() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Execution getExecution() {
        return execution;
    }

    public void setExecution(Execution execution) {
        this.execution = execution;
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
}
