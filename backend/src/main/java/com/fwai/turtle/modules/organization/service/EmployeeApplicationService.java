package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.EmployeeApplicationDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeDTO;
import com.fwai.turtle.modules.workflow.dto.WorkflowTaskDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalHistoryDTO;
import com.fwai.turtle.base.types.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * EmployeeApplicationService
 * 员工入职申请服务接口
 */
public interface EmployeeApplicationService {
    
    /**
     * 创建入职申请（GUEST用户使用）
     */
    EmployeeApplicationDTO createApplication(EmployeeApplicationDTO applicationDTO, Long applicantUserId);
    
    /**
     * 更新入职申请
     */
    EmployeeApplicationDTO updateApplication(Long id, EmployeeApplicationDTO applicationDTO);
    
    /**
     * 根据ID查找申请
     */
    Optional<EmployeeApplicationDTO> findById(Long id);
    
    /**
     * 获取所有申请（分页）
     */
    Page<EmployeeApplicationDTO> findAll(Pageable pageable);
    
    /**
     * 根据状态查找申请
     */
    Page<EmployeeApplicationDTO> findByStatus(ApplicationStatus status, Pageable pageable);
    
    /**
     * 获取待审核申请
     */
    Page<EmployeeApplicationDTO> findPendingApplications(Pageable pageable);
    
    /**
     * 根据申请人ID查找申请
     */
    List<EmployeeApplicationDTO> findByApplicantUserId(Long applicantUserId);
    
    /**
     * 审核申请
     */
    EmployeeApplicationDTO reviewApplication(Long id, ApplicationStatus newStatus, String reviewComments, Long reviewerUserId);
    
    /**
     * 批准申请并转换为员工记录
     */
    EmployeeDTO approveAndConvertToEmployee(Long applicationId, EmployeeDTO employeeInfo, Long reviewerUserId);
    
    /**
     * 删除申请
     */
    void deleteApplication(Long id);
    
    /**
     * 检查身份证号是否已存在
     */
    boolean existsByIdNumber(String idNumber);
    
    /**
     * 检查邮箱是否有待处理的申请
     */
    boolean hasPendingApplicationByEmail(String email);
    
    /**
     * 根据状态统计申请数量
     */
    long countByStatus(ApplicationStatus status);
    
    /**
     * 获取申请统计信息
     */
    ApplicationStatisticsDTO getApplicationStatistics();
    
    /**
     * 获取用户的工作流待办任务
     * 
     * @param userId 用户ID
     * @return 待办任务列表
     */
    List<WorkflowTaskDTO> getUserPendingTasks(Long userId);
    
    /**
     * 获取申请的审批历史
     * 
     * @param applicationId 申请ID
     * @return 审批历史列表
     */
    List<ApprovalHistoryDTO> getApprovalHistory(Long applicationId);
    
    /**
     * 获取申请的当前审批任务
     * 
     * @param applicationId 申请ID
     * @return 当前审批任务
     */
    WorkflowTaskDTO getCurrentApprovalTask(Long applicationId);
    
    /**
     * 申请统计信息DTO
     */
    class ApplicationStatisticsDTO {
        private long totalApplications;
        private long pendingCount;
        private long underReviewCount;
        private long approvedCount;
        private long rejectedCount;
        private long validatedCount;
        
        // getters and setters
        public long getTotalApplications() { return totalApplications; }
        public void setTotalApplications(long totalApplications) { this.totalApplications = totalApplications; }
        
        public long getPendingCount() { return pendingCount; }
        public void setPendingCount(long pendingCount) { this.pendingCount = pendingCount; }
        
        public long getUnderReviewCount() { return underReviewCount; }
        public void setUnderReviewCount(long underReviewCount) { this.underReviewCount = underReviewCount; }
        
        public long getApprovedCount() { return approvedCount; }
        public void setApprovedCount(long approvedCount) { this.approvedCount = approvedCount; }
        
        public long getRejectedCount() { return rejectedCount; }
        public void setRejectedCount(long rejectedCount) { this.rejectedCount = rejectedCount; }
        
        public long getValidatedCount() { return validatedCount; }
        public void setValidatedCount(long validatedCount) { this.validatedCount = validatedCount; }
    }
}