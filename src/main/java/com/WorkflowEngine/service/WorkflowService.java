package com.WorkflowEngine.service;

import com.WorkflowEngine.dto.PageResponse;
import com.WorkflowEngine.dto.RuleDTO;
import com.WorkflowEngine.dto.StepDTO;
import com.WorkflowEngine.dto.WorkflowDTO;
import com.WorkflowEngine.dto.WorkflowRequest;
import com.WorkflowEngine.exception.ResourceNotFoundException;
import com.WorkflowEngine.model.Rule;
import com.WorkflowEngine.model.Step;
import com.WorkflowEngine.model.Workflow;
import com.WorkflowEngine.repository.RuleRepository;
import com.WorkflowEngine.repository.StepRepository;
import com.WorkflowEngine.repository.WorkflowRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final StepRepository stepRepository;
    private final RuleRepository ruleRepository;

    public WorkflowService(WorkflowRepository workflowRepository,
                           StepRepository stepRepository,
                           RuleRepository ruleRepository) {
        this.workflowRepository = workflowRepository;
        this.stepRepository = stepRepository;
        this.ruleRepository = ruleRepository;
    }

    public PageResponse<WorkflowDTO> getWorkflows(int page, int size, String search, Boolean active) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Workflow> wfPage;

        boolean hasSearch = search != null && !search.trim().isEmpty();

        if (hasSearch && active != null) {
            wfPage = workflowRepository.findByNameContainingIgnoreCaseAndIsActive(search.trim(), active, pageable);
        } else if (hasSearch) {
            wfPage = workflowRepository.findByNameContainingIgnoreCase(search.trim(), pageable);
        } else if (active != null) {
            wfPage = workflowRepository.findByIsActive(active, pageable);
        } else {
            wfPage = workflowRepository.findAll(pageable);
        }

        Page<WorkflowDTO> dtoPage = wfPage.map(this::toSummaryDTO);
        return PageResponse.of(dtoPage);
    }

    public WorkflowDTO getById(UUID id) {
        Workflow wf = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", id));
        return toDetailDTO(wf);
    }

    @Transactional
    public WorkflowDTO create(WorkflowRequest request) {
        Workflow wf = new Workflow();
        wf.setName(request.getName());
        wf.setInputSchema(request.getInputSchema());
        wf.setStartStepId(request.getStartStepId());
        wf.setIsActive(request.getIsActive() != null ? request.getIsActive() : true);
        wf.setVersion(1);

        return toSummaryDTO(workflowRepository.save(wf));
    }

    @Transactional
    public WorkflowDTO update(UUID id, WorkflowRequest request) {
        Workflow wf = workflowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", id));

        wf.setName(request.getName());
        wf.setInputSchema(request.getInputSchema());

        if (request.getStartStepId() != null) {
            wf.setStartStepId(request.getStartStepId());
        }

        if (request.getIsActive() != null) {
            wf.setIsActive(request.getIsActive());
        }

        wf.setVersion(wf.getVersion() + 1);

        return toDetailDTO(workflowRepository.save(wf));
    }

    @Transactional
    public void delete(UUID id) {
        if (!workflowRepository.existsById(id)) {
            throw new ResourceNotFoundException("Workflow", id);
        }
        workflowRepository.deleteById(id);
    }

    private WorkflowDTO toSummaryDTO(Workflow wf) {
        List<Step> steps = stepRepository.findByWorkflowIdOrderByStepOrderAsc(wf.getId());

        WorkflowDTO dto = new WorkflowDTO();
        dto.setId(wf.getId());
        dto.setName(wf.getName());
        dto.setVersion(wf.getVersion());
        dto.setIsActive(wf.getIsActive());
        dto.setInputSchema(wf.getInputSchema());
        dto.setStartStepId(wf.getStartStepId());
        dto.setCreatedAt(wf.getCreatedAt());
        dto.setUpdatedAt(wf.getUpdatedAt());
        dto.setStepCount(steps.size());

        return dto;
    }

    private WorkflowDTO toDetailDTO(Workflow wf) {
        List<Step> steps = stepRepository.findByWorkflowIdOrderByStepOrderAsc(wf.getId());
        List<StepDTO> stepDTOs = new ArrayList<>();

        for (Step step : steps) {
            stepDTOs.add(toStepDTO(step));
        }

        WorkflowDTO dto = new WorkflowDTO();
        dto.setId(wf.getId());
        dto.setName(wf.getName());
        dto.setVersion(wf.getVersion());
        dto.setIsActive(wf.getIsActive());
        dto.setInputSchema(wf.getInputSchema());
        dto.setStartStepId(wf.getStartStepId());
        dto.setCreatedAt(wf.getCreatedAt());
        dto.setUpdatedAt(wf.getUpdatedAt());
        dto.setStepCount(steps.size());
        dto.setSteps(stepDTOs);

        return dto;
    }

    private StepDTO toStepDTO(Step step) {
        List<Rule> rules = ruleRepository.findByStepIdOrderByPriorityAsc(step.getId());
        List<RuleDTO> ruleDTOs = new ArrayList<>();

        for (Rule rule : rules) {
            ruleDTOs.add(toRuleDTO(rule));
        }

        StepDTO dto = new StepDTO();
        dto.setId(step.getId());
        dto.setWorkflowId(step.getWorkflow().getId());
        dto.setName(step.getName());
        dto.setStepType(step.getStepType());
        dto.setStepOrder(step.getStepOrder());
        dto.setMetadata(step.getMetadata());
        dto.setCreatedAt(step.getCreatedAt());
        dto.setRules(ruleDTOs);

        return dto;
    }

    private RuleDTO toRuleDTO(Rule rule) {
        RuleDTO dto = new RuleDTO();
        dto.setId(rule.getId());
        dto.setStepId(rule.getStep().getId());
        dto.setCondition(rule.getCondition());
        dto.setNextStepId(rule.getNextStepId());
        dto.setPriority(rule.getPriority());
        dto.setCreatedAt(rule.getCreatedAt());

        return dto;
    }
}
