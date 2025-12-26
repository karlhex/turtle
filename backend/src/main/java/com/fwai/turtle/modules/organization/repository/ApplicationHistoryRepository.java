package com.fwai.turtle.modules.organization.repository;

import com.fwai.turtle.modules.organization.entity.ApplicationHistory;
import com.fwai.turtle.modules.organization.entity.ApplicationHistory.ActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ApplicationHistoryRepository
 * 申请操作历史记录数据访问层
 */
@Repository
public interface ApplicationHistoryRepository extends JpaRepository<ApplicationHistory, Long> {

    /**
     * 根据申请ID查找历史记录，按创建时间倒序
     */
    List<ApplicationHistory> findByApplication_IdOrderByCreatedAtDesc(Long applicationId);

    /**
     * 根据申请ID分页查找历史记录
     */
    Page<ApplicationHistory> findByApplication_IdOrderByCreatedAtDesc(Long applicationId, Pageable pageable);

    /**
     * 根据操作人ID查找历史记录
     */
    Page<ApplicationHistory> findByOperator_IdOrderByCreatedAtDesc(Long operatorId, Pageable pageable);

    /**
     * 根据操作类型查找历史记录
     */
    Page<ApplicationHistory> findByActionTypeOrderByCreatedAtDesc(ActionType actionType, Pageable pageable);

    /**
     * 查找指定时间范围内的历史记录
     */
    @Query("SELECT ah FROM ApplicationHistory ah WHERE ah.createdAt BETWEEN :startDate AND :endDate ORDER BY ah.createdAt DESC")
    Page<ApplicationHistory> findByCreatedAtBetween(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate,
        Pageable pageable
    );

    /**
     * 根据申请ID和操作类型查找历史记录
     */
    List<ApplicationHistory> findByApplication_IdAndActionTypeOrderByCreatedAtDesc(Long applicationId, ActionType actionType);

    /**
     * 查找申请的最新历史记录
     */
    @Query("SELECT ah FROM ApplicationHistory ah WHERE ah.application.id = :applicationId ORDER BY ah.createdAt DESC")
    List<ApplicationHistory> findLatestByApplicationId(@Param("applicationId") Long applicationId, Pageable pageable);

    /**
     * 统计指定申请的历史记录数量
     */
    long countByApplication_Id(Long applicationId);

    /**
     * 统计指定操作类型的记录数量
     */
    long countByActionType(ActionType actionType);

    /**
     * 统计指定时间范围内的操作数量
     */
    @Query("SELECT COUNT(ah) FROM ApplicationHistory ah WHERE ah.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * 查找指定申请的状态变更历史
     */
    @Query("SELECT ah FROM ApplicationHistory ah WHERE ah.application.id = :applicationId AND ah.actionType IN ('STATUS_CHANGED', 'REVIEWED', 'APPROVED', 'REJECTED') ORDER BY ah.createdAt DESC")
    List<ApplicationHistory> findStatusChangeHistory(@Param("applicationId") Long applicationId);

    /**
     * 根据工作流任务ID查找历史记录
     */
    List<ApplicationHistory> findByWorkflowTaskIdOrderByCreatedAtDesc(String workflowTaskId);

    /**
     * 删除指定申请的所有历史记录（级联删除时使用）
     */
    void deleteByApplication_Id(Long applicationId);
}