package com.WorkflowEngine.dto;

import java.util.Map;

public class StepRequest {
    private String name;
    private String stepType;
    private Integer stepOrder;
    private Map<String, Object> metadata;

    public StepRequest() {
    }

    public StepRequest(String name, String stepType, Integer stepOrder, Map<String, Object> metadata) {
        this.name = name;
        this.stepType = stepType;
        this.stepOrder = stepOrder;
        this.metadata = metadata;
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
}
