package com.fwai.turtle.modules.organization.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.service.UserService;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationService.ApplicationStatisticsDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeApplicationDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeDTO;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationService;
import com.fwai.turtle.modules.organization.service.ApplicationHistoryService;
import com.fwai.turtle.modules.organization.dto.ApplicationHistoryDTO;
import com.fwai.turtle.modules.workflow.dto.WorkflowTaskDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalHistoryDTO;
import com.fwai.turtle.base.types.ApplicationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * EmployeeApplicationController
 * 员工入职申请控制器
 */
@RestController
@RequestMapping("/api/employee-applications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Employee Applications", description = "员工入职申请管理")
public class EmployeeApplicationController {

    private final EmployeeApplicationService employeeApplicationService;
    private final UserService userService;
    private final ApplicationHistoryService historyService;

    @PostMapping
    @Operation(summary = "提交入职申请", description = "GUEST用户提交员工入职申请")
    @PreAuthorize("hasRole('GUEST') or hasAuthority('hr.application.create')")
    public ApiResponse<EmployeeApplicationDTO> submitApplication(
            @Valid @RequestBody EmployeeApplicationDTO applicationDTO,
            Principal principal) {
        
        log.info("Submitting employee application for user: {}", principal.getName());
        
        // Get actual user ID from principal
        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long applicantUserId = user.getId();
        
        EmployeeApplicationDTO createdApplication = employeeApplicationService
            .createApplication(applicationDTO, applicantUserId);
            
        return ApiResponse.ok(createdApplication);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新入职申请", description = "申请人更新自己的入职申请（仅限未审核或需要补充资料状态）")
    @PreAuthorize("hasRole('GUEST') or hasAuthority('hr.application.update')")
    public ApiResponse<EmployeeApplicationDTO> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeApplicationDTO applicationDTO) {
        
        log.info("Updating employee application: {}", id);
        
        EmployeeApplicationDTO updatedApplication = employeeApplicationService
            .updateApplication(id, applicationDTO);
            
        return ApiResponse.ok(updatedApplication);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查看申请详情", description = "查看指定的入职申请详情")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<EmployeeApplicationDTO> getApplication(@PathVariable Long id) {
        log.info("Getting employee application: {}", id);
        
        return employeeApplicationService.findById(id)
            .map(ApiResponse::ok)
            .orElse(ApiResponse.error(HttpStatus.NOT_FOUND.value(), "申请不存在"));
    }

    @GetMapping
    @Operation(summary = "查询所有申请", description = "HR人员查看所有入职申请")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<Page<EmployeeApplicationDTO>> getAllApplications(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting all employee applications with pageable: {}", pageable);
        
        Page<EmployeeApplicationDTO> applications = employeeApplicationService.findAll(pageable);
        return ApiResponse.ok(applications);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "按状态查询申请", description = "按申请状态查询入职申请")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<Page<EmployeeApplicationDTO>> getApplicationsByStatus(
            @Parameter(description = "申请状态") @PathVariable ApplicationStatus status,
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting employee applications by status: {}", status);
        
        Page<EmployeeApplicationDTO> applications = employeeApplicationService
            .findByStatus(status, pageable);
        return ApiResponse.ok(applications);
    }

    @GetMapping("/pending")
    @Operation(summary = "获取待处理申请", description = "获取所有待处理的入职申请（已提交、审核中、需要补充资料）")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<Page<EmployeeApplicationDTO>> getPendingApplications(
            @PageableDefault(size = 20) Pageable pageable) {
        
        log.info("Getting pending employee applications");
        
        Page<EmployeeApplicationDTO> applications = employeeApplicationService
            .findPendingApplications(pageable);
        return ApiResponse.ok(applications);
    }

    @GetMapping("/my-applications")
    @Operation(summary = "获取我的申请", description = "GUEST用户查看自己提交的申请")
    @PreAuthorize("hasRole('GUEST') or hasAuthority('hr.application.read')")
    public ApiResponse<List<EmployeeApplicationDTO>> getMyApplications(Principal principal) {
        log.info("Getting applications for user: {}", principal.getName());
        
        // Get actual user ID from principal
        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long applicantUserId = user.getId();
        
        List<EmployeeApplicationDTO> applications = employeeApplicationService
            .findByApplicantUserId(applicantUserId);
        return ApiResponse.ok(applications);
    }

    @PostMapping("/{id}/review")
    @Operation(summary = "审核申请", description = "HR人员审核入职申请")
    @PreAuthorize("hasAuthority('hr.application.approve')")
    public ApiResponse<EmployeeApplicationDTO> reviewApplication(
            @PathVariable Long id,
            @Parameter(description = "审核结果") @RequestParam ApplicationStatus status,
            @Parameter(description = "审核意见") @RequestParam(required = false) String comments,
            Principal principal) {
        
        log.info("Reviewing employee application: {} with status: {}", id, status);
        
        // Get actual reviewer user ID from principal
        User reviewer = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long reviewerUserId = reviewer.getId();
        
        EmployeeApplicationDTO reviewedApplication = employeeApplicationService
            .reviewApplication(id, status, comments, reviewerUserId);
            
        return ApiResponse.ok(reviewedApplication);
    }

    @PostMapping("/{id}/approve-and-convert")
    @Operation(summary = "批准并转为员工", description = "批准申请并创建员工记录")
    @PreAuthorize("hasAuthority('hr.application.approve')")
    public ApiResponse<EmployeeDTO> approveAndConvertToEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeDTO employeeInfo,
            Principal principal) {
        
        log.info("Approving and converting application {} to employee", id);
        
        // Get actual reviewer user ID from principal
        User reviewer = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long reviewerUserId = reviewer.getId();
        
        EmployeeDTO createdEmployee = employeeApplicationService
            .approveAndConvertToEmployee(id, employeeInfo, reviewerUserId);
            
        return ApiResponse.ok(createdEmployee);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除申请", description = "删除入职申请（仅限管理员）")
    @PreAuthorize("hasAuthority('hr.application.delete')")
    public ApiResponse<Void> deleteApplication(@PathVariable Long id) {
        log.info("Deleting employee application: {}", id);

        employeeApplicationService.deleteApplication(id);
        return ApiResponse.ok(null);
    }

    // ==================== 历史记录相关API ====================

    @GetMapping("/{id}/history")
    @Operation(summary = "获取申请操作历史", description = "获取指定申请的完整操作历史记录")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<List<ApplicationHistoryDTO>> getApplicationHistory(@PathVariable Long id) {
        log.info("Getting application history for: {}", id);

        List<ApplicationHistoryDTO> histories = historyService.getApplicationHistory(id);
        return ApiResponse.ok(histories);
    }

    @GetMapping("/{id}/status-history")
    @Operation(summary = "获取申请状态变更历史", description = "获取指定申请的状态变更历史记录")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<List<ApplicationHistoryDTO>> getApplicationStatusHistory(@PathVariable Long id) {
        log.info("Getting application status history for: {}", id);

        List<ApplicationHistoryDTO> histories = historyService.getApplicationStatusHistory(id);
        return ApiResponse.ok(histories);
    }

    @GetMapping("/{id}/history/pageable")
    @Operation(summary = "分页获取申请历史", description = "分页获取指定申请的历史记录")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<Page<ApplicationHistoryDTO>> getApplicationHistoryPage(
            @PathVariable Long id,
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Getting application history page for: {}, page: {}", id, pageable);

        Page<ApplicationHistoryDTO> historyPage = historyService.getApplicationHistory(id, pageable);
        return ApiResponse.ok(historyPage);
    }

    @GetMapping("/{id}/history/latest")
    @Operation(summary = "获取最新历史记录", description = "获取指定申请的最新历史记录")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<List<ApplicationHistoryDTO>> getLatestHistory(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int limit) {
        log.info("Getting latest history for: {}, limit: {}", id, limit);

        List<ApplicationHistoryDTO> histories = historyService.getLatestHistory(id, limit);
        return ApiResponse.ok(histories);
    }

    @GetMapping("/{id}/history/count")
    @Operation(summary = "获取历史记录数量", description = "获取指定申请的历史记录总数")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<Long> getHistoryCount(@PathVariable Long id) {
        log.info("Getting history count for: {}", id);

        long count = historyService.countApplicationHistory(id);
        return ApiResponse.ok(count);
    }

    @GetMapping("/statistics")
    @Operation(summary = "获取申请统计", description = "获取各状态申请的统计信息")
    @PreAuthorize("hasAuthority('hr.application.read')")
    public ApiResponse<ApplicationStatisticsDTO> getApplicationStatistics() {
        log.info("Getting application statistics");
        
        ApplicationStatisticsDTO statistics = employeeApplicationService
            .getApplicationStatistics();
        return ApiResponse.ok(statistics);
    }

    @GetMapping("/check-id-number/{idNumber}")
    @Operation(summary = "检查身份证号", description = "检查身份证号是否已存在")
    public ApiResponse<Boolean> checkIdNumber(@PathVariable String idNumber) {
        log.info("Checking ID number: {}", idNumber);
        
        boolean exists = employeeApplicationService.existsByIdNumber(idNumber);
        return ApiResponse.ok(exists);
    }

    @GetMapping("/check-pending-email/{email}")
    @Operation(summary = "检查待处理邮箱", description = "检查邮箱是否有待处理申请")
    public ApiResponse<Boolean> checkPendingEmail(@PathVariable String email) {
        log.info("Checking pending email: {}", email);
        
        boolean hasPending = employeeApplicationService.hasPendingApplicationByEmail(email);
        return ApiResponse.ok(hasPending);
    }

    @GetMapping("/my-pending-tasks")
    @Operation(summary = "获取我的待办任务", description = "获取当前用户的工作流待办任务")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<List<WorkflowTaskDTO>> getMyPendingTasks(Principal principal) {
        log.info("Getting pending tasks for user: {}", principal.getName());
        
        // Get actual user ID from principal
        User user = userService.findByUsername(principal.getName())
            .orElseThrow(() -> new RuntimeException("User not found: " + principal.getName()));
        Long userId = user.getId();
        
        List<WorkflowTaskDTO> tasks = employeeApplicationService.getUserPendingTasks(userId);
        return ApiResponse.ok(tasks);
    }

    @GetMapping("/{id}/approval-history")
    @Operation(summary = "获取申请审批历史", description = "获取指定申请的审批历史记录")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<List<ApprovalHistoryDTO>> getApprovalHistory(@PathVariable Long id) {
        log.info("Getting approval history for application: {}", id);
        
        List<ApprovalHistoryDTO> history = employeeApplicationService.getApprovalHistory(id);
        return ApiResponse.ok(history);
    }

    @GetMapping("/{id}/current-task")
    @Operation(summary = "获取申请当前任务", description = "获取指定申请的当前审批任务")
    @PreAuthorize("!hasRole('GUEST')")
    public ApiResponse<WorkflowTaskDTO> getCurrentApprovalTask(@PathVariable Long id) {
        log.info("Getting current approval task for application: {}", id);
        
        WorkflowTaskDTO currentTask = employeeApplicationService.getCurrentApprovalTask(id);
        return ApiResponse.ok(currentTask);
    }
}