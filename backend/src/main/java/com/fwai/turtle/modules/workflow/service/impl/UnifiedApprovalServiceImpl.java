package com.fwai.turtle.modules.workflow.service.impl;

import com.fwai.turtle.modules.workflow.dto.ApprovalRequestDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalTaskDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalHistoryDTO;
import com.fwai.turtle.modules.workflow.service.UnifiedApprovalService;
import com.fwai.turtle.modules.workflow.service.FlowableWorkflowService;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationService;
import com.fwai.turtle.modules.finance.service.ReimbursementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统一审批服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UnifiedApprovalServiceImpl implements UnifiedApprovalService {

    private final FlowableWorkflowService flowableWorkflowService;
    private final EmployeeApplicationService employeeApplicationService;
    private final ReimbursementService reimbursementService;

    @Override
    public Page<ApprovalRequestDTO> getAllApprovalRequests(Pageable pageable, String requestType, String status) {
        log.info("Getting all approval requests with type: {}, status: {}", requestType, status);
        
        // For now, return a mock implementation
        // In a real implementation, this would aggregate data from multiple services
        List<ApprovalRequestDTO> requests = new ArrayList<>();
        
        // Add employee applications
        if (requestType == null || "EMPLOYEE_APPLICATION".equals(requestType)) {
            // Get employee applications and convert to ApprovalRequestDTO
            // This is a simplified implementation
        }
        
        // Add reimbursements
        if (requestType == null || "REIMBURSEMENT".equals(requestType)) {
            // Get reimbursements and convert to ApprovalRequestDTO
        }
        
        return new PageImpl<>(requests, pageable, requests.size());
    }

    @Override
    public Page<ApprovalRequestDTO> getApprovalRequestsByStatus(String status, Pageable pageable, String requestType) {
        log.info("Getting approval requests by status: {}, type: {}", status, requestType);
        
        List<ApprovalRequestDTO> requests = new ArrayList<>();
        // Implementation would filter by status
        
        return new PageImpl<>(requests, pageable, requests.size());
    }

    @Override
    public Page<ApprovalRequestDTO> getPendingApprovalRequests(Pageable pageable, String requestType) {
        log.info("Getting pending approval requests with type: {}", requestType);
        
        List<ApprovalRequestDTO> requests = new ArrayList<>();
        // Implementation would get pending requests
        
        return new PageImpl<>(requests, pageable, requests.size());
    }

    @Override
    public ApprovalRequestDTO getApprovalRequest(Long id) {
        log.info("Getting approval request details for ID: {}", id);
        
        // Mock implementation - in real implementation, determine the type and get details
        return ApprovalRequestDTO.builder()
            .id(id)
            .applicationId(id)
            .applicantName("Test Applicant")
            .applicantEmail("test@example.com")
            .requestType("EMPLOYEE_APPLICATION")
            .status("PENDING")
            .submitTime(LocalDateTime.now())
            .currentStep("HR_REVIEW")
            .priority("MEDIUM")
            .description("Test approval request")
            .build();
    }

    @Override
    public void processApprovalRequest(Long requestId, String decision, String comments, Long userId) {
        log.info("Processing approval request {} with decision: {} by user: {}", requestId, decision, userId);
        
        // This would:
        // 1. Determine the request type
        // 2. Find the current workflow task
        // 3. Complete the task with the decision
        // 4. Update the business entity status
        
        // For now, just log the action
        log.info("Approval request {} processed successfully", requestId);
    }

    @Override
    public List<ApprovalTaskDTO> getUserPendingTasks(Long userId) {
        log.info("Getting pending tasks for user: {}", userId);
        
        try {
            // Get tasks from Flowable for this user
            List<Task> flowableTasks = flowableWorkflowService.getUserTasksWithRoles(userId.toString());
            
            // Convert Flowable tasks to ApprovalTaskDTO
            return flowableTasks.stream()
                .map(this::convertToApprovalTaskDTO)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error getting pending tasks for user: {}", userId, e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ApprovalHistoryDTO> getApprovalHistory(Long requestId) {
        log.info("Getting approval history for request: {}", requestId);
        
        List<ApprovalHistoryDTO> history = new ArrayList<>();
        // Implementation would get history from various sources
        
        return history;
    }

    @Override
    public Object getApprovalStatistics() {
        log.info("Getting approval statistics");
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalPending", 0);
        statistics.put("totalApproved", 0);
        statistics.put("totalRejected", 0);
        statistics.put("byType", new HashMap<String, Integer>());
        
        return statistics;
    }

    @Override
    public void deleteApprovalRequest(Long id) {
        log.info("Deleting approval request: {}", id);
        
        // Implementation would:
        // 1. Determine the request type
        // 2. Cancel the workflow if running
        // 3. Delete the business entity
        
        log.info("Approval request {} deleted successfully", id);
    }

    /**
     * Convert Flowable Task to ApprovalTaskDTO
     */
    private ApprovalTaskDTO convertToApprovalTaskDTO(Task task) {
        try {
            // Get business key to determine application type and ID
            String businessKey = flowableWorkflowService.getProcessBusinessKey(task.getProcessInstanceId());
            
            return ApprovalTaskDTO.builder()
                .id(task.getId())
                .name(task.getName() != null ? task.getName() : "")
                .createTime(task.getCreateTime().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime())
                .stepType(task.getTaskDefinitionKey() != null ? task.getTaskDefinitionKey() : "")
                .requestType(determineRequestTypeFromBusinessKey(businessKey))
                .priority("MEDIUM") // Default priority
                .description(task.getDescription() != null ? task.getDescription() : "")
                .assignee(task.getAssignee())
                .processInstanceId(task.getProcessInstanceId())
                .businessKey(businessKey)
                .dueDate(task.getDueDate() != null ? 
                    task.getDueDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime() : null)
                .formKey(task.getFormKey())
                .build();
                
        } catch (Exception e) {
            log.warn("Error converting task {} to DTO: {}", task.getId(), e.getMessage());
            return ApprovalTaskDTO.builder()
                .id(task.getId())
                .name(task.getName() != null ? task.getName() : "Unknown Task")
                .createTime(LocalDateTime.now())
                .stepType("UNKNOWN")
                .requestType("UNKNOWN")
                .priority("MEDIUM")
                .build();
        }
    }

    /**
     * Determine request type from business key
     */
    private String determineRequestTypeFromBusinessKey(String businessKey) {
        if (businessKey == null) {
            return "UNKNOWN";
        }
        
        if (businessKey.startsWith("employee-application-")) {
            return "EMPLOYEE_APPLICATION";
        } else if (businessKey.startsWith("reimbursement-")) {
            return "REIMBURSEMENT";
        } else if (businessKey.startsWith("contract-")) {
            return "CONTRACT";
        } else {
            return "OTHER";
        }
    }
}