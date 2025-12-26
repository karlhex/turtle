package com.fwai.turtle.modules.workflow.service;

import com.fwai.turtle.modules.workflow.dto.ApprovalRequestDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalTaskDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalHistoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 统一审批服务接口
 * 提供跨业务模块的统一审批功能
 */
public interface UnifiedApprovalService {

    /**
     * 获取所有审批请求（分页）
     */
    Page<ApprovalRequestDTO> getAllApprovalRequests(Pageable pageable, String requestType, String status);

    /**
     * 按状态获取审批请求
     */
    Page<ApprovalRequestDTO> getApprovalRequestsByStatus(String status, Pageable pageable, String requestType);

    /**
     * 获取待处理审批请求
     */
    Page<ApprovalRequestDTO> getPendingApprovalRequests(Pageable pageable, String requestType);

    /**
     * 获取审批请求详情
     */
    ApprovalRequestDTO getApprovalRequest(Long id);

    /**
     * 处理审批请求
     */
    void processApprovalRequest(Long requestId, String decision, String comments, Long userId);

    /**
     * 获取用户待办任务
     */
    List<ApprovalTaskDTO> getUserPendingTasks(Long userId);

    /**
     * 获取审批历史
     */
    List<ApprovalHistoryDTO> getApprovalHistory(Long requestId);

    /**
     * 获取审批统计
     */
    Object getApprovalStatistics();

    /**
     * 删除审批请求
     */
    void deleteApprovalRequest(Long id);
}