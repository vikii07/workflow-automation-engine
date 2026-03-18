package com.WorkflowEngine.service;

import com.WorkflowEngine.dto.ExecutionDTO;
import com.WorkflowEngine.dto.ExecutionLogDTO;
import com.WorkflowEngine.dto.ExecutionRequest;
import com.WorkflowEngine.dto.PageResponse;
import com.WorkflowEngine.engine.WorkflowExecutionEngine;
import com.WorkflowEngine.exception.ResourceNotFoundException;
import com.WorkflowEngine.model.Execution;
import com.WorkflowEngine.model.ExecutionLog;
import com.WorkflowEngine.model.Step;
import com.WorkflowEngine.model.Workflow;
import com.WorkflowEngine.repository.ExecutionLogRepository;
import com.WorkflowEngine.repository.ExecutionRepository;
import com.WorkflowEngine.repository.StepRepository;
import com.WorkflowEngine.repository.WorkflowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ExecutionLogRepository executionLogRepository;
    private final WorkflowRepository workflowRepository;
    private final StepRepository stepRepository;
    private final WorkflowExecutionEngine executionEngine;

    public ExecutionService(ExecutionRepository executionRepository,
                            ExecutionLogRepository executionLogRepository,
                            WorkflowRepository workflowRepository,
                            StepRepository stepRepository,
                            WorkflowExecutionEngine executionEngine) {
        this.executionRepository = executionRepository;
        this.executionLogRepository = executionLogRepository;
        this.workflowRepository = workflowRepository;
        this.stepRepository = stepRepository;
        this.executionEngine = executionEngine;
    }

    @Transactional
    public ExecutionDTO startExecution(UUID workflowId, ExecutionRequest request) {
        Execution execution = executionEngine.execute(
                workflowId,
                request.getData(),
                request.getTriggeredBy()
        );
        return toDTO(execution, true);
    }

    public ExecutionDTO getById(UUID id) {
        Execution execution = executionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Execution", id));
        return toDTO(execution, true);
    }

    public PageResponse<ExecutionDTO> getByWorkflow(UUID workflowId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<Execution> execPage = executionRepository.findByWorkflowIdAndIsArchivedFalse(workflowId, pageable);
        Page<ExecutionDTO> dtoPage = execPage.map(e -> toDTO(e, false));
        return PageResponse.of(dtoPage);
    }

    public PageResponse<ExecutionDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<Execution> execPage = executionRepository.findByIsArchivedFalse(pageable);
        Page<ExecutionDTO> dtoPage = execPage.map(e -> toDTO(e, false));
        return PageResponse.of(dtoPage);
    }

    public PageResponse<ExecutionDTO> getArchived(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startedAt").descending());
        Page<Execution> execPage = executionRepository.findByIsArchivedTrue(pageable);
        Page<ExecutionDTO> dtoPage = execPage.map(e -> toDTO(e, false));
        return PageResponse.of(dtoPage);
    }

    @Transactional
    public ExecutionDTO cancel(UUID id) {
        Execution execution = executionEngine.cancel(id);
        return toDTO(execution, false);
    }

    @Transactional
    public ExecutionDTO retry(UUID id) {
        Execution execution = executionEngine.retry(id);
        return toDTO(execution, true);
    }

    @Transactional
    public ExecutionDTO archive(UUID id) {
        Execution execution = executionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Execution", id));

        execution.setIsArchived(true);
        executionRepository.save(execution);

        return toDTO(execution, false);
    }

    private ExecutionDTO toDTO(Execution execution, boolean includeLogs) {
        String workflowName = workflowRepository.findById(execution.getWorkflowId())
                .map(w -> w.getName())
                .orElse("Unknown");

        String currentStepName = null;
        if (execution.getCurrentStepId() != null) {
            currentStepName = stepRepository.findById(execution.getCurrentStepId())
                    .map(s -> s.getName())
                    .orElse(null);
        }

        List<ExecutionLogDTO> logs = null;
        if (includeLogs) {
            logs = new ArrayList<>();
            List<ExecutionLog> executionLogs =
                    executionLogRepository.findByExecutionIdOrderByStartedAtAsc(execution.getId());
            for (ExecutionLog log : executionLogs) {
                logs.add(toLogDTO(log));
            }
        }

        ExecutionDTO dto = new ExecutionDTO();
        dto.setId(execution.getId());
        dto.setWorkflowId(execution.getWorkflowId());
        dto.setWorkflowName(workflowName);
        dto.setWorkflowVersion(execution.getWorkflowVersion());
        dto.setStatus(execution.getStatus());
        dto.setData(execution.getData());
        dto.setCurrentStepId(execution.getCurrentStepId());
        dto.setCurrentStepName(currentStepName);
        dto.setRetries(execution.getRetries());
        dto.setStartedAt(execution.getStartedAt());
        dto.setEndedAt(execution.getEndedAt());
        dto.setLogs(logs);
        return dto;
    }

    private ExecutionLogDTO toLogDTO(ExecutionLog log) {
        long durationMs = 0;
        if (log.getStartedAt() != null && log.getEndedAt() != null) {
            durationMs = Duration.between(log.getStartedAt(), log.getEndedAt()).toMillis();
        }

        String nextStepName = null;
        if (log.getSelectedNextStep() != null) {
            nextStepName = stepRepository.findById(log.getSelectedNextStep())
                    .map(s -> s.getName())
                    .orElse(null);
        }

        ExecutionLogDTO dto = new ExecutionLogDTO();
        dto.setId(log.getId());
        dto.setExecutionId(log.getExecution().getId());
        dto.setStepName(log.getStepName());
        dto.setStepType(log.getStepType());
        dto.setEvaluatedRules(log.getEvaluatedRules());
        dto.setSelectedNextStep(log.getSelectedNextStep());
        dto.setSelectedNextStepName(nextStepName);
        dto.setStatus(log.getStatus());
        dto.setApproverId(log.getApproverId());
        dto.setErrorMessage(log.getErrorMessage());
        dto.setStartedAt(log.getStartedAt());
        dto.setEndedAt(log.getEndedAt());
        dto.setDurationMs(durationMs);
        return dto;
    }
}
