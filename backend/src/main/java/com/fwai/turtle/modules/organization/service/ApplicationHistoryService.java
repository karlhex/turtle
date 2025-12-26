package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.dto.ApplicationHistoryDTO;
import com.fwai.turtle.modules.organization.entity.ApplicationHistory.ActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ApplicationHistoryService
 * 申请操作历史记录服务接口
 */
public interface ApplicationHistoryService {

    /**
     * 记录申请操作历史
     * @param applicationId 申请ID
     * @param actionType 操作类型
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @param operatorId 操作人ID
     * @param description 操作描述
     * @param details 操作详情（JSON格式）
     * @return 历史记录DTO
     */
    ApplicationHistoryDTO recordHistory(Long applicationId, ActionType actionType,
                                      ApplicationStatus fromStatus, ApplicationStatus toStatus,
                                      Long operatorId, String description, String details);

    /**
     * 记录申请操作历史（简化版本）
     * @param applicationId 申请ID
     * @param actionType 操作类型
     * @param operatorId 操作人ID
     * @param description 操作描述
     * @return 历史记录DTO
     */
    ApplicationHistoryDTO recordHistory(Long applicationId, ActionType actionType,
                                      Long operatorId, String description);

    /**
     * 记录系统操作历史
     * @param applicationId 申请ID
     * @param actionType 操作类型
     * @param description 操作描述
     * @return 历史记录DTO
     */
    ApplicationHistoryDTO recordSystemHistory(Long applicationId, ActionType actionType, String description);

    /**
     * 记录工作流操作历史
     * @param applicationId 申请ID
     * @param actionType 操作类型
     * @param workflowTaskId 工作流任务ID
     * @param operatorId 操作人ID
     * @param description 操作描述
     * @return 历史记录DTO
     */
    ApplicationHistoryDTO recordWorkflowHistory(Long applicationId, ActionType actionType,
                                              String workflowTaskId, Long operatorId, String description);

    /**
     * 获取申请的所有历史记录
     * @param applicationId 申请ID
     * @return 历史记录列表
     */
    List<ApplicationHistoryDTO> getApplicationHistory(Long applicationId);

    /**
     * 分页获取申请的历史记录
     * @param applicationId 申请ID
     * @param pageable 分页参数
     * @return 分页历史记录
     */
    Page<ApplicationHistoryDTO> getApplicationHistory(Long applicationId, Pageable pageable);

    /**
     * 获取申请的状态变更历史
     * @param applicationId 申请ID
     * @return 状态变更历史列表
     */
    List<ApplicationHistoryDTO> getApplicationStatusHistory(Long applicationId);

    /**
     * 根据操作人获取历史记录
     * @param operatorId 操作人ID
     * @param pageable 分页参数
     * @return 分页历史记录
     */
    Page<ApplicationHistoryDTO> getHistoryByOperator(Long operatorId, Pageable pageable);

    /**
     * 根据操作类型获取历史记录
     * @param actionType 操作类型
     * @param pageable 分页参数
     * @return 分页历史记录
     */
    Page<ApplicationHistoryDTO> getHistoryByActionType(ActionType actionType, Pageable pageable);

    /**
     * 获取指定时间范围内的历史记录
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param pageable 分页参数
     * @return 分页历史记录
     */
    Page<ApplicationHistoryDTO> getHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * 获取申请的最新历史记录
     * @param applicationId 申请ID
     * @param limit 记录数量限制
     * @return 最新历史记录列表
     */
    List<ApplicationHistoryDTO> getLatestHistory(Long applicationId, int limit);

    /**
     * 删除申请的所有历史记录
     * @param applicationId 申请ID
     */
    void deleteApplicationHistory(Long applicationId);

    /**
     * 统计申请的历史记录数量
     * @param applicationId 申请ID
     * @return 历史记录数量
     */
    long countApplicationHistory(Long applicationId);

    /**
     * 获取操作统计信息
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @return 操作统计映射（操作类型 -> 数量）
     */
    java.util.Map<ActionType, Long> getOperationStatistics(LocalDateTime startDate, LocalDateTime endDate);
}