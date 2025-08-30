package com.fwai.turtle.modules.workflow.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.workflow.service.FlowableWorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Flowable 工作流管理控制器
 * 提供 Flowable 引擎的管理和监控 API
 */
@RestController
@RequestMapping("/api/flowable")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Flowable Management", description = "Flowable workflow engine management APIs")
@ConditionalOnProperty(value = "workflow.engine", havingValue = "flowable")
public class FlowableController {

    private final FlowableWorkflowService flowableWorkflowService;

    @GetMapping("/process-definitions")
    @Operation(summary = "Get all process definitions")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow.admin')")
    public ApiResponse<List<Map<String, Object>>> getProcessDefinitions() {
        try {
            List<ProcessDefinition> definitions = flowableWorkflowService.getAllProcessDefinitions();
            List<Map<String, Object>> result = definitions.stream()
                .map(this::convertProcessDefinition)
                .collect(Collectors.toList());
            return ApiResponse.ok(result);
        } catch (Exception e) {
            log.error("Error getting process definitions", e);
            return ApiResponse.error(30014,"Failed to get process definitions: " + e.getMessage());
        }
    }

    @GetMapping("/tasks")
    @Operation(summary = "Get tasks for user", description = "Retrieve tasks for a user by their ID (numeric). Also supports username or email as fallback.")
    public ApiResponse<List<Map<String, Object>>> getTasks(
            @RequestParam String userId) {
        try {
            log.info("Getting tasks for user ID: {}", userId);
            
            // Get all tasks related to this user (assigned + candidate tasks based on database roles)
            List<Task> tasks = flowableWorkflowService.getUserTasksWithRoles(userId);
            
            List<Map<String, Object>> result = tasks.stream()
                .map(this::convertTask)
                .collect(Collectors.toList());
                
            log.info("Found {} tasks for user ID: {}", result.size(), userId);
            return ApiResponse.ok(result);
        } catch (Exception e) {
            log.error("Error getting tasks for user ID: {}", userId, e);
            return ApiResponse.error(30014,"Failed to get tasks: " + e.getMessage());
        }
    }
    
    /**
     * 获取所有活动任务（用于管理员查看）
     */
    private List<Task> getAllActiveTasks() {
        try {
            return flowableWorkflowService.getAllActiveTasks();
        } catch (Exception e) {
            log.warn("Failed to get all active tasks, returning empty list", e);
            return List.of();
        }
    }

    @PostMapping("/tasks/{taskId}/complete")
    @Operation(summary = "Complete a task")
    public ApiResponse<String> completeTask(
            @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> variables) {
        try {
            flowableWorkflowService.completeTask(taskId, variables);
            return ApiResponse.ok("Task completed successfully");
        } catch (Exception e) {
            log.error("Error completing task", e);
            return ApiResponse.error(30014,"Failed to complete task: " + e.getMessage());
        }
    }

    @PostMapping("/tasks/{taskId}/claim")
    @Operation(summary = "Claim a task", description = "Claim a task by user ID (numeric). Also supports username as fallback.")
    public ApiResponse<String> claimTask(
            @PathVariable String taskId,
            @RequestParam String userId) {
        try {
            log.info("User ID {} claiming task {}", userId, taskId);
            flowableWorkflowService.claimTask(taskId, userId);
            return ApiResponse.ok("Task claimed successfully by user ID: " + userId);
        } catch (Exception e) {
            log.error("Error claiming task {} by user ID {}", taskId, userId, e);
            return ApiResponse.error(30014,"Failed to claim task: " + e.getMessage());
        }
    }

    @PostMapping("/tasks/{taskId}/unclaim")
    @Operation(summary = "Unclaim a task")
    public ApiResponse<String> unclaimTask(@PathVariable String taskId) {
        try {
            flowableWorkflowService.unclaimTask(taskId);
            return ApiResponse.ok("Task unclaimed successfully");
        } catch (Exception e) {
            log.error("Error unclaiming task", e);
            return ApiResponse.error(30014,"Failed to unclaim task: " + e.getMessage());
        }
    }

    @GetMapping("/process-instances")
    @Operation(summary = "Get process instances")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow.admin')")
    public ApiResponse<List<Map<String, Object>>> getProcessInstances(
            @RequestParam(required = false) String businessKey,
            @RequestParam(required = false) String processDefinitionKey) {
        try {
            // This would need implementation in FlowableWorkflowService
            return ApiResponse.ok(List.of());
        } catch (Exception e) {
            log.error("Error getting process instances", e);
            return ApiResponse.error(30014,"Failed to get process instances: " + e.getMessage());
        }
    }

    @PostMapping("/process-instances")
    @Operation(summary = "Start process instance")
    public ApiResponse<Map<String, Object>> startProcessInstance(
            @RequestParam String processDefinitionKey,
            @RequestParam(required = false) String businessKey,
            @RequestBody(required = false) Map<String, Object> variables) {
        try {
            String processInstanceId = businessKey != null 
                ? flowableWorkflowService.startProcessWithBusinessKey(processDefinitionKey, businessKey, variables)
                : flowableWorkflowService.startProcess(processDefinitionKey, variables);
            
            ProcessInstance instance = flowableWorkflowService.getProcessInstance(processInstanceId);
            return ApiResponse.ok(convertProcessInstance(instance));
        } catch (Exception e) {
            log.error("Error starting process instance", e);
            return ApiResponse.error(30014,"Failed to start process instance: " + e.getMessage());
        }
    }

    @DeleteMapping("/process-instances/{processInstanceId}")
    @Operation(summary = "Delete process instance")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow.admin')")
    public ApiResponse<String> deleteProcessInstance(
            @PathVariable String processInstanceId,
            @RequestParam(required = false) String deleteReason) {
        try {
            flowableWorkflowService.deleteProcessInstance(processInstanceId, deleteReason);
            return ApiResponse.ok("Process instance deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting process instance", e);
            return ApiResponse.error(30014,"Failed to delete process instance: " + e.getMessage());
        }
    }

    @PutMapping("/process-instances/{processInstanceId}/suspend")
    @Operation(summary = "Suspend process instance")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow.admin')")
    public ApiResponse<String> suspendProcessInstance(@PathVariable String processInstanceId) {
        try {
            flowableWorkflowService.suspendProcessInstance(processInstanceId);
            return ApiResponse.ok("Process instance suspended successfully");
        } catch (Exception e) {
            log.error("Error suspending process instance", e);
            return ApiResponse.error(30014,"Failed to suspend process instance: " + e.getMessage());
        }
    }

    @PutMapping("/process-instances/{processInstanceId}/activate")
    @Operation(summary = "Activate process instance")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow.admin')")
    public ApiResponse<String> activateProcessInstance(@PathVariable String processInstanceId) {
        try {
            flowableWorkflowService.activateProcessInstance(processInstanceId);
            return ApiResponse.ok("Process instance activated successfully");
        } catch (Exception e) {
            log.error("Error activating process instance", e);
            return ApiResponse.error(30014,"Failed to activate process instance: " + e.getMessage());
        }
    }

    @GetMapping("/process-instances/{processInstanceId}/diagram")
    @Operation(summary = "Get process diagram")
    public ResponseEntity<byte[]> getProcessDiagram(@PathVariable String processInstanceId) {
        try {
            // This would need implementation in FlowableWorkflowService
            return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(new byte[0]);
        } catch (Exception e) {
            log.error("Error getting process diagram", e);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/history/process-instances")
    @Operation(summary = "Get historic process instances")
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('workflow.admin')")
    public ApiResponse<List<Map<String, Object>>> getHistoricProcessInstances(
            @RequestParam(required = false) String processInstanceId,
            @RequestParam(required = false) String businessKey) {
        try {
            // This would need implementation in FlowableWorkflowService
            return ApiResponse.ok(List.of());
        } catch (Exception e) {
            log.error("Error getting historic process instances", e);
            return ApiResponse.error(30014,"Failed to get historic process instances: " + e.getMessage());
        }
    }

    // Helper methods to convert Flowable objects to Maps
    private Map<String, Object> convertProcessDefinition(ProcessDefinition definition) {
        return Map.of(
            "id", definition.getId(),
            "key", definition.getKey(),
            "name", definition.getName() != null ? definition.getName() : definition.getKey(),
            "version", definition.getVersion(),
            "deploymentId", definition.getDeploymentId(),
            "suspended", definition.isSuspended(),
            "category", definition.getCategory() != null ? definition.getCategory() : "",
            "description", definition.getDescription() != null ? definition.getDescription() : ""
        );
    }

    /**
     * Debug endpoint for testing task retrieval without authentication
     */
    @GetMapping("/debug-tasks")
    @Operation(summary = "Debug task retrieval", description = "Test task retrieval logic without authentication (dev only)")
    public ApiResponse<Map<String, Object>> debugTasks(@RequestParam(required = false) String userId) {
        try {
            Map<String, Object> result = new java.util.HashMap<>();
            
            // Get all active tasks
            List<Task> allTasks = flowableWorkflowService.getAllActiveTasks();
            result.put("totalActiveTasks", allTasks.size());
            result.put("allTaskIds", allTasks.stream().map(Task::getId).collect(Collectors.toList()));
            result.put("allTasks", allTasks.stream().map(this::convertTask).collect(Collectors.toList()));
            
            // Show candidate group breakdown
            List<Task> deptTasks = flowableWorkflowService.getCandidateGroupTasks("DEPT_LEADER");
            List<Task> financeTasks = flowableWorkflowService.getCandidateGroupTasks("FINANCE_MANAGER");
            List<Task> generalTasks = flowableWorkflowService.getCandidateGroupTasks("GENERAL_MANAGER");
            
            result.put("deptLeaderTasksCount", deptTasks.size());
            result.put("financeManagerTasksCount", financeTasks.size());
            result.put("generalManagerTasksCount", generalTasks.size());
            
            if (userId != null) {
                // Get detailed user info
                Map<String, Object> userDebugInfo = flowableWorkflowService.getUserDebugInfo(userId);
                result.put("userDebugInfo", userDebugInfo);
                
                // Test our enhanced method for specific user
                List<Task> userTasks = flowableWorkflowService.getUserTasksWithRoles(userId);
                result.put("userTasksCount", userTasks.size());
                result.put("userTasks", userTasks.stream().map(this::convertTask).collect(Collectors.toList()));
                
                // Test individual methods
                List<Task> assignedTasks = flowableWorkflowService.getPendingTasks(userId);
                List<Task> candidateTasks = flowableWorkflowService.getCandidateTasks(userId);
                
                result.put("userAssignedTasksCount", assignedTasks.size());
                result.put("userCandidateTasksCount", candidateTasks.size());
            }
            
            return ApiResponse.ok(result);
        } catch (Exception e) {
            log.error("Error in debug-tasks", e);
            return ApiResponse.error(30014,"Debug task error: " + e.getMessage());
        }
    }

    private Map<String, Object> convertTask(Task task) {
        // Get candidate groups for this task
        List<String> candidateGroups = new java.util.ArrayList<>();
        try {
            candidateGroups = flowableWorkflowService.getTaskCandidateGroups(task.getId());
        } catch (Exception e) {
            log.warn("Could not get candidate groups for task {}: {}", task.getId(), e.getMessage());
        }
        
        Map<String, Object> taskMap = new java.util.HashMap<>();
        taskMap.put("id", task.getId());
        taskMap.put("name", task.getName() != null ? task.getName() : "");
        taskMap.put("assignee", task.getAssignee() != null ? task.getAssignee() : "");
        taskMap.put("candidateGroups", candidateGroups);
        taskMap.put("createTime", task.getCreateTime().toString());
        taskMap.put("processInstanceId", task.getProcessInstanceId());
        taskMap.put("processDefinitionId", task.getProcessDefinitionId());
        taskMap.put("taskDefinitionKey", task.getTaskDefinitionKey() != null ? task.getTaskDefinitionKey() : "");
        taskMap.put("description", task.getDescription() != null ? task.getDescription() : "");
        taskMap.put("priority", task.getPriority());
        taskMap.put("dueDate", task.getDueDate() != null ? task.getDueDate().toString() : null);
        return taskMap;
    }

    private Map<String, Object> convertProcessInstance(ProcessInstance instance) {
        return Map.of(
            "id", instance.getId(),
            "processDefinitionId", instance.getProcessDefinitionId(),
            "processDefinitionKey", instance.getProcessDefinitionKey(),
            "businessKey", instance.getBusinessKey() != null ? instance.getBusinessKey() : "",
            "startTime", instance.getStartTime().toString(),
            "startUserId", instance.getStartUserId() != null ? instance.getStartUserId() : "",
            "suspended", instance.isSuspended(),
            "ended", instance.isEnded(),
            "processDefinitionName", instance.getProcessDefinitionName() != null ? instance.getProcessDefinitionName() : ""
        );
    }
}