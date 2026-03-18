package com.WorkflowEngine.controller;

import com.WorkflowEngine.dto.ApiResponse;
import com.WorkflowEngine.dto.StepDTO;
import com.WorkflowEngine.dto.StepRequest;
import com.WorkflowEngine.service.StepService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class StepController {

    private final StepService stepService;

    public StepController(StepService stepService) {
        this.stepService = stepService;
    }

    @GetMapping("/workflows/{workflowId}/steps")
    public ResponseEntity<ApiResponse<List<StepDTO>>> listByWorkflow(@PathVariable UUID workflowId) {
        return ResponseEntity.ok(ApiResponse.success(stepService.getStepsByWorkflow(workflowId)));
    }

    @GetMapping("/steps/{id}")
    public ResponseEntity<ApiResponse<StepDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(stepService.getById(id)));
    }

    @PostMapping("/workflows/{workflowId}/steps")
    public ResponseEntity<ApiResponse<StepDTO>> create(
            @PathVariable UUID workflowId,
            @RequestBody StepRequest request) {
        StepDTO created = stepService.create(workflowId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Step created", created));
    }

    @PutMapping("/steps/{id}")
    public ResponseEntity<ApiResponse<StepDTO>> update(
            @PathVariable UUID id,
            @RequestBody StepRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Step updated", stepService.update(id, request)));
    }

    @DeleteMapping("/steps/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        stepService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Step deleted", null));
    }
}
