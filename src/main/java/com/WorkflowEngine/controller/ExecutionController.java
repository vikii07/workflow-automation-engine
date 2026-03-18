package com.WorkflowEngine.controller;

import com.WorkflowEngine.dto.ApiResponse;
import com.WorkflowEngine.dto.ExecutionDTO;
import com.WorkflowEngine.dto.ExecutionRequest;
import com.WorkflowEngine.dto.PageResponse;
import com.WorkflowEngine.service.ExecutionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @PostMapping("/workflows/{workflowId}/execute")
    public ResponseEntity<ApiResponse<ExecutionDTO>> execute(
            @PathVariable UUID workflowId,
            @RequestBody(required = false) ExecutionRequest request) {
        if (request == null) request = new ExecutionRequest();
        ExecutionDTO execution = executionService.startExecution(workflowId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Execution started", execution));
    }

    @GetMapping("/executions")
    public ResponseEntity<ApiResponse<PageResponse<ExecutionDTO>>> listAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(executionService.getAll(page, size)));
    }

    @GetMapping("/executions/archived")
    public ResponseEntity<ApiResponse<PageResponse<ExecutionDTO>>> listArchived(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.success(executionService.getArchived(page, size)));
    }

    @GetMapping("/workflows/{workflowId}/executions")
    public ResponseEntity<ApiResponse<PageResponse<ExecutionDTO>>> listByWorkflow(
            @PathVariable UUID workflowId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                executionService.getByWorkflow(workflowId, page, size)));
    }

    @GetMapping("/executions/{id}")
    public ResponseEntity<ApiResponse<ExecutionDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(executionService.getById(id)));
    }

    @PostMapping("/executions/{id}/cancel")
    public ResponseEntity<ApiResponse<ExecutionDTO>> cancel(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Execution cancelled", executionService.cancel(id)));
    }

    @PostMapping("/executions/{id}/retry")
    public ResponseEntity<ApiResponse<ExecutionDTO>> retry(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Execution retried", executionService.retry(id)));
    }

    @PostMapping("/executions/{id}/archive")
    public ResponseEntity<ApiResponse<ExecutionDTO>> archive(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Execution archived", executionService.archive(id)));
    }
}
