package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;

/**
 * EmployeeApplicationApprovalService
 * 员工入职申请审批服务接口 - 供工作流引擎调用
 */
public interface EmployeeApplicationApprovalService {
    
    /**
     * 更新申请状态
     * 
     * @param applicationId 申请ID
     * @param status 新状态
     * @return 是否更新成功
     */
    boolean updateStatus(Long applicationId, String status);
    
    /**
     * 记录审批意见
     * 
     * @param applicationId 申请ID
     * @param reviewerId 审批人ID
     * @param comments 审批意见
     * @param decision 审批决定
     */
    void recordApprovalComment(Long applicationId, String reviewerId, String comments, String decision);
    
    /**
     * 获取申请详情
     * 
     * @param applicationId 申请ID
     * @return 申请实体
     */
    EmployeeApplication getApplication(Long applicationId);
    
    /**
     * 检查申请是否存在
     * 
     * @param applicationId 申请ID
     * @return 是否存在
     */
    boolean applicationExists(Long applicationId);
    
    /**
     * 获取申请人用户ID
     * 
     * @param applicationId 申请ID
     * @return 申请人用户ID
     */
    Long getApplicantUserId(Long applicationId);
    
    /**
     * 完成入职办理
     * 
     * @param applicationId 申请ID
     * @return 是否完成成功
     */
    boolean completeOnboarding(Long applicationId);
}