package com.WorkflowEngine.service;

import com.WorkflowEngine.dto.RuleDTO;
import com.WorkflowEngine.dto.StepDTO;
import com.WorkflowEngine.dto.StepRequest;
import com.WorkflowEngine.exception.ResourceNotFoundException;
import com.WorkflowEngine.model.Rule;
import com.WorkflowEngine.model.Step;
import com.WorkflowEngine.model.Workflow;
import com.WorkflowEngine.repository.RuleRepository;
import com.WorkflowEngine.repository.StepRepository;
import com.WorkflowEngine.repository.WorkflowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class StepService {

    private final StepRepository stepRepository;
    private final WorkflowRepository workflowRepository;
    private final RuleRepository ruleRepository;

    public StepService(StepRepository stepRepository,
                       WorkflowRepository workflowRepository,
                       RuleRepository ruleRepository) {
        this.stepRepository = stepRepository;
        this.workflowRepository = workflowRepository;
        this.ruleRepository = ruleRepository;
    }

    public List<StepDTO> getStepsByWorkflow(UUID workflowId) {
        if (!workflowRepository.existsById(workflowId)) {
            throw new ResourceNotFoundException("Workflow", workflowId);
        }

        List<Step> steps = stepRepository.findByWorkflowIdOrderByStepOrderAsc(workflowId);
        List<StepDTO> result = new ArrayList<>();
        for (Step step : steps) {
            result.add(toDTO(step));
        }
        return result;
    }

    public StepDTO getById(UUID id) {
        Step step = stepRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Step", id));
        return toDTO(step);
    }

    @Transactional
    public StepDTO create(UUID workflowId, StepRequest request) {
        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", workflowId));

        int order = request.getStepOrder() != null
                ? request.getStepOrder()
                : stepRepository.findByWorkflowIdOrderByStepOrderAsc(workflowId).size() + 1;

        Step step = new Step();
        step.setWorkflow(workflow);
        step.setName(request.getName());
        step.setStepType(request.getStepType());
        step.setStepOrder(order);
        step.setMetadata(request.getMetadata());

        return toDTO(stepRepository.save(step));
    }

    @Transactional
    public StepDTO update(UUID id, StepRequest request) {
        Step step = stepRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Step", id));

        step.setName(request.getName());
        step.setStepType(request.getStepType());
        if (request.getStepOrder() != null) {
            step.setStepOrder(request.getStepOrder());
        }
        if (request.getMetadata() != null) {
            step.setMetadata(request.getMetadata());
        }

        return toDTO(stepRepository.save(step));
    }

    @Transactional
    public void delete(UUID id) {
        if (!stepRepository.existsById(id)) {
            throw new ResourceNotFoundException("Step", id);
        }
        stepRepository.deleteById(id);
    }

    private StepDTO toDTO(Step step) {
        List<Rule> rules = ruleRepository.findByStepIdOrderByPriorityAsc(step.getId());
        List<RuleDTO> ruleDTOs = new ArrayList<>();

        for (Rule r : rules) {
            RuleDTO dto = new RuleDTO();
            dto.setId(r.getId());
            dto.setStepId(step.getId());
            dto.setCondition(r.getCondition());
            dto.setNextStepId(r.getNextStepId());
            dto.setPriority(r.getPriority());
            dto.setCreatedAt(r.getCreatedAt());
            ruleDTOs.add(dto);
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
}
