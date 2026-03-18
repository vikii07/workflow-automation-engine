package com.WorkflowEngine.dto;

import java.util.UUID;

public class RuleRequest {
    private String condition;
    private UUID nextStepId;
    private Integer priority;

    public RuleRequest() {
    }

    public RuleRequest(String condition, UUID nextStepId, Integer priority) {
        this.condition = condition;
        this.nextStepId = nextStepId;
        this.priority = priority;
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
}
