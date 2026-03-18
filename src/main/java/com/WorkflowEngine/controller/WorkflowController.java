package com.WorkflowEngine.controller;

import com.WorkflowEngine.dto.ApiResponse;
import com.WorkflowEngine.dto.PageResponse;
import com.WorkflowEngine.dto.WorkflowDTO;
import com.WorkflowEngine.dto.WorkflowRequest;
import com.WorkflowEngine.service.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workflows")
@CrossOrigin(origins = "*")
public class WorkflowController {

    private final WorkflowService workflowService;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<WorkflowDTO>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(ApiResponse.success(
                workflowService.getWorkflows(page, size, search, active)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkflowDTO>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(workflowService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<WorkflowDTO>> create(@RequestBody WorkflowRequest request) {
        WorkflowDTO created = workflowService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Workflow created", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkflowDTO>> update(
            @PathVariable UUID id,
            @RequestBody WorkflowRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Workflow updated", workflowService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        workflowService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Workflow deleted", null));
    }
}
