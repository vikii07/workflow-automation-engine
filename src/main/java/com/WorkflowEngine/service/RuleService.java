package com.WorkflowEngine.service;

import com.WorkflowEngine.dto.RuleDTO;
import com.WorkflowEngine.dto.RuleRequest;
import com.WorkflowEngine.exception.ResourceNotFoundException;
import com.WorkflowEngine.model.Rule;
import com.WorkflowEngine.model.Step;
import com.WorkflowEngine.repository.RuleRepository;
import com.WorkflowEngine.repository.StepRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class RuleService {

    private final RuleRepository ruleRepository;
    private final StepRepository stepRepository;

    public RuleService(RuleRepository ruleRepository, StepRepository stepRepository) {
        this.ruleRepository = ruleRepository;
        this.stepRepository = stepRepository;
    }

    public List<RuleDTO> getRulesByStep(UUID stepId) {
        if (!stepRepository.existsById(stepId)) {
            throw new ResourceNotFoundException("Step", stepId);
        }

        List<Rule> rules = ruleRepository.findByStepIdOrderByPriorityAsc(stepId);
        List<RuleDTO> result = new ArrayList<>();
        for (Rule rule : rules) {
            result.add(toDTO(rule));
        }
        return result;
    }

    public RuleDTO getById(UUID id) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule", id));
        return toDTO(rule);
    }

    @Transactional
    public RuleDTO create(UUID stepId, RuleRequest request) {
        Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step", stepId));

        int priority = request.getPriority() != null
                ? request.getPriority()
                : ruleRepository.findByStepIdOrderByPriorityAsc(stepId).size() + 1;

        Rule rule = new Rule();
        rule.setStep(step);
        rule.setCondition(request.getCondition());
        rule.setNextStepId(request.getNextStepId());
        rule.setPriority(priority);

        return toDTO(ruleRepository.save(rule));
    }

    @Transactional
    public RuleDTO update(UUID id, RuleRequest request) {
        Rule rule = ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule", id));

        rule.setCondition(request.getCondition());
        rule.setNextStepId(request.getNextStepId());
        if (request.getPriority() != null) {
            rule.setPriority(request.getPriority());
        }

        return toDTO(ruleRepository.save(rule));
    }

    @Transactional
    public void delete(UUID id) {
        if (!ruleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rule", id);
        }
        ruleRepository.deleteById(id);
    }

    private RuleDTO toDTO(Rule rule) {
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
