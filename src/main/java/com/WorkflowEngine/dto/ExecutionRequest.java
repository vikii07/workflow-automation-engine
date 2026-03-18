package com.WorkflowEngine.dto;

import java.util.Map;
import java.util.UUID;

public class ExecutionRequest {
    private Map<String, Object> data;
    private UUID triggeredBy;

    public ExecutionRequest() {
    }

    public ExecutionRequest(Map<String, Object> data, UUID triggeredBy) {
        this.data = data;
        this.triggeredBy = triggeredBy;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public UUID getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(UUID triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
}
