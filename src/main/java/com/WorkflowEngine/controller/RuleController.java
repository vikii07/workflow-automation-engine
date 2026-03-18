package com.WorkflowEngine.controller;

import com.WorkflowEngine.dto.ApiResponse;
import com.WorkflowEngine.dto.RuleDTO;
import com.WorkflowEngine.dto.RuleRequest;
import com.WorkflowEngine.service.RuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @GetMapping("/steps/{stepId}/rules")
    public ResponseEntity<ApiResponse<List<RuleDTO>>> listByStep(@PathVariable UUID stepId) {
        return ResponseEntity.ok(ApiResponse.success(ruleService.getRulesByStep(stepId)));
    }

    @GetMapping("/rules/{id}")
    public ResponseEntity<ApiResponse<RuleDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(ruleService.getById(id)));
    }

    @PostMapping("/steps/{stepId}/rules")
    public ResponseEntity<ApiResponse<RuleDTO>> create(
            @PathVariable UUID stepId,
            @RequestBody RuleRequest request) {
        RuleDTO created = ruleService.create(stepId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Rule created", created));
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<ApiResponse<RuleDTO>> update(
            @PathVariable UUID id,
            @RequestBody RuleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Rule updated", ruleService.update(id, request)));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        ruleService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Rule deleted", null));
    }
}
