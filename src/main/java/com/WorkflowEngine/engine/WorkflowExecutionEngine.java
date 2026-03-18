package com.WorkflowEngine.engine;

import com.WorkflowEngine.model.Execution;
import com.WorkflowEngine.model.ExecutionLog;
import com.WorkflowEngine.model.Rule;
import com.WorkflowEngine.model.Step;
import com.WorkflowEngine.model.Workflow;
import com.WorkflowEngine.repository.ExecutionLogRepository;
import com.WorkflowEngine.repository.ExecutionRepository;
import com.WorkflowEngine.repository.RuleRepository;
import com.WorkflowEngine.repository.StepRepository;
import com.WorkflowEngine.repository.WorkflowRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class WorkflowExecutionEngine {

    private final WorkflowRepository workflowRepository;
    private final StepRepository stepRepository;
    private final RuleRepository ruleRepository;
    private final ExecutionRepository executionRepository;
    private final ExecutionLogRepository executionLogRepository;
    private final RuleEngine ruleEngine;

    public WorkflowExecutionEngine(WorkflowRepository workflowRepository,
                                   StepRepository stepRepository,
                                   RuleRepository ruleRepository,
                                   ExecutionRepository executionRepository,
                                   ExecutionLogRepository executionLogRepository,
                                   RuleEngine ruleEngine) {
        this.workflowRepository = workflowRepository;
        this.stepRepository = stepRepository;
        this.ruleRepository = ruleRepository;
        this.executionRepository = executionRepository;
        this.executionLogRepository = executionLogRepository;
        this.ruleEngine = ruleEngine;
    }

    @Transactional
    public Execution execute(UUID workflowId, Map<String, Object> inputData, UUID triggeredBy) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found: " + workflowId));

        if (!workflow.getIsActive()) {
            throw new IllegalStateException("Workflow is not active: " + workflowId);
        }

        if (workflow.getStartStepId() == null) {
            throw new IllegalStateException("Workflow has no start step configured");
        }

        Execution execution = new Execution();
        execution.setWorkflowId(workflowId);
        execution.setWorkflowVersion(workflow.getVersion());
        execution.setStatus("RUNNING");
        execution.setData(inputData != null ? inputData : new HashMap<>());
        execution.setCurrentStepId(workflow.getStartStepId());
        execution.setTriggeredBy(triggeredBy);
        execution.setStartedAt(LocalDateTime.now());

        execution = executionRepository.save(execution);

        try {
            UUID currentStepId = workflow.getStartStepId();
            int maxSteps = 100;
            int stepCount = 0;

            while (currentStepId != null && stepCount < maxSteps) {
                stepCount++;

                UUID stepId = currentStepId;

                Step step = stepRepository.findById(stepId)
                        .orElseThrow(() -> new IllegalStateException("Step not found: " + stepId));

                ExecutionLog stepLog = new ExecutionLog();
                stepLog.setExecution(execution);
                stepLog.setStepName(step.getName());
                stepLog.setStepType(step.getStepType());
                stepLog.setStartedAt(LocalDateTime.now());

                UUID nextStepId = null;
                String logStatus = "SUCCESS";
                String errorMsg = null;
                List<Map<String, Object>> evalLog = new ArrayList<>();

                try {
                    executeStep(step, execution.getData());

                    List<Rule> rules = ruleRepository.findByStepIdOrderByPriorityAsc(step.getId());
                    RuleEngine.RuleEvaluationResult result = ruleEngine.evaluate(rules, execution.getData());
                    evalLog = result.evaluationLog();

                    if (result.hasMatch()) {
                        nextStepId = result.matchedRule().getNextStepId();
                    }

                } catch (Exception ex) {
                    logStatus = "FAILED";
                    errorMsg = ex.getMessage();
                }

                stepLog.setEvaluatedRules(evalLog);
                stepLog.setSelectedNextStep(nextStepId);
                stepLog.setStatus(logStatus);
                stepLog.setErrorMessage(errorMsg);
                stepLog.setEndedAt(LocalDateTime.now());
                executionLogRepository.save(stepLog);

                execution.setCurrentStepId(nextStepId);
                executionRepository.save(execution);

                if ("FAILED".equals(logStatus)) {
                    execution.setStatus("FAILED");
                    execution.setEndedAt(LocalDateTime.now());
                    executionRepository.save(execution);
                    return execution;
                }

                currentStepId = nextStepId;
            }
            if (stepCount >= maxSteps) {
                execution.setStatus("FAILED");
            } else {
                execution.setStatus("COMPLETED");
            }

            execution.setEndedAt(LocalDateTime.now());
            executionRepository.save(execution);

        } catch (Exception ex) {
            execution.setStatus("FAILED");
            execution.setEndedAt(LocalDateTime.now());
            executionRepository.save(execution);
        }

        return execution;
    }

    @Transactional
    public Execution cancel(UUID executionId) {
        Execution execution = executionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException("Execution not found: " + executionId));

        if ("COMPLETED".equals(execution.getStatus()) || "CANCELLED".equals(execution.getStatus())) {
            throw new IllegalStateException("Cannot cancel execution in status: " + execution.getStatus());
        }

        execution.setStatus("CANCELLED");
        execution.setEndedAt(LocalDateTime.now());
        return executionRepository.save(execution);
    }

    @Transactional
    public Execution retry(UUID executionId) {
        Execution execution = executionRepository.findById(executionId)
                .orElseThrow(() -> new IllegalArgumentException("Execution not found: " + executionId));

        if (!"FAILED".equals(execution.getStatus())) {
            throw new IllegalStateException("Only FAILED executions can be retried");
        }

        execution.setStatus("RUNNING");
        execution.setRetries(execution.getRetries() + 1);
        execution.setEndedAt(null);
        executionRepository.save(execution);

        return execute(execution.getWorkflowId(), execution.getData(), execution.getTriggeredBy());
    }

    private void executeStep(Step step, Map<String, Object> data) {
        if (data != null) {
            data.put("lastExecutedStep", step.getName());
        }
    }
}
