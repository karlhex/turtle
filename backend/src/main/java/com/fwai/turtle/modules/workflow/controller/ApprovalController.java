package com.fwai.turtle.modules.workflow.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.service.UserService;
import com.fwai.turtle.modules.workflow.dto.ApprovalRequestDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalTaskDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalHistoryDTO;
import com.fwai.turtle.modules.workflow.service.UnifiedApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * 统一审批控制器
 * 提供跨业务模块的统一审批API
 */
@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Unified Approvals", description = "统一审批管理API")
public class ApprovalController {

    private final UnifiedApprovalService unifiedApprovalService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "获取所有审批请求", description = "分页获取所有审批请求")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<Page<ApprovalRequestDTO>> getAllApprovalRequests(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String requestType,
            @RequestParam(required = false) String status) {
        
        log.info("Getting all approval requests with type: {}, status: {}", requestType, status);
        
        Page<ApprovalRequestDTO> requests = unifiedApprovalService.getAllApprovalRequests(
            pageable, requestType, status);
        return ApiResponse.ok(requests);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "按状态获取审批请求", description = "按指定状态获取审批请求")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<Page<ApprovalRequestDTO>> getApprovalRequestsByStatus(
            @Parameter(description = "审批状态") @PathVariable String status,
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String requestType) {
        
        log.info("Getting approval requests by status: {}, type: {}", status, requestType);
        
        Page<ApprovalRequestDTO> requests = unifiedApprovalService.getApprovalRequestsByStatus(
            status, pageable, requestType);
        return ApiResponse.ok(requests);
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待处理审批请求", description = "获取所有待处理的审批请求")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<Page<ApprovalRequestDTO>> getPendingApprovalRequests(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String requestType) {
        
        log.info("Getting pending approval requests with type: {}", requestType);
        
        Page<ApprovalRequestDTO> requests = unifiedApprovalService.getPendingApprovalRequests(
            pageable, requestType);
        return ApiResponse.ok(requests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取审批请求详情", description = "根据ID获取审批请求详情")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<ApprovalRequestDTO> getApprovalRequest(
            @Parameter(description = "审批请求ID") @PathVariable Long id) {
        
        log.info("Getting approval request details for ID: {}", id);
        
        ApprovalRequestDTO request = unifiedApprovalService.getApprovalRequest(id);
        return ApiResponse.ok(request);
    }

    @PostMapping("/{requestId}/process")
    @Operation(summary = "处理审批请求", description = "处理指定的审批请求")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<String> processApprovalRequest(
            @Parameter(description = "审批请求ID") @PathVariable Long requestId,
            @Parameter(description = "审批决定") @RequestParam String decision,
            @Parameter(description = "审批意见") @RequestParam(required = false) String comments,
            Principal principal) {
        
        log.info("Processing approval request {} with decision: {}", requestId, decision);
        
        // Get actual user ID from principal
        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long userId = user.getId();
        
        unifiedApprovalService.processApprovalRequest(requestId, decision, comments, userId);
        return ApiResponse.ok("Approval request processed successfully");
    }

    @GetMapping("/my-tasks")
    @Operation(summary = "获取我的待办任务", description = "获取当前用户的所有待办审批任务")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<List<ApprovalTaskDTO>> getMyPendingTasks(Principal principal) {
        log.info("Getting pending tasks for user: {}", principal.getName());
        
        // Get actual user ID from principal
        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long userId = user.getId();
        
        List<ApprovalTaskDTO> tasks = unifiedApprovalService.getUserPendingTasks(userId);
        return ApiResponse.ok(tasks);
    }

    @GetMapping("/{requestId}/history")
    @Operation(summary = "获取审批历史", description = "获取指定审批请求的历史记录")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<List<ApprovalHistoryDTO>> getApprovalHistory(
            @Parameter(description = "审批请求ID") @PathVariable Long requestId) {
        
        log.info("Getting approval history for request: {}", requestId);
        
        List<ApprovalHistoryDTO> history = unifiedApprovalService.getApprovalHistory(requestId);
        return ApiResponse.ok(history);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取审批统计", description = "获取审批请求的统计信息")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<Object> getApprovalStatistics() {
        log.info("Getting approval statistics");
        
        Object statistics = unifiedApprovalService.getApprovalStatistics();
        return ApiResponse.ok(statistics);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除审批请求", description = "删除指定的审批请求（仅限管理员）")
    @PreAuthorize("hasAuthority('APPROVAL_DELETE') or hasRole('ADMIN')")
    public ApiResponse<Void> deleteApprovalRequest(
            @Parameter(description = "审批请求ID") @PathVariable Long id) {
        
        log.info("Deleting approval request: {}", id);
        
        unifiedApprovalService.deleteApprovalRequest(id);
        return ApiResponse.ok(null);
    }
}