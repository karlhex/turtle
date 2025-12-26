package com.fwai.turtle.modules.organization.repository;

import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.base.types.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * EmployeeApplicationRepository
 * 员工入职申请数据访问层
 */
@Repository
public interface EmployeeApplicationRepository extends JpaRepository<EmployeeApplication, Long> {
    
    /**
     * 根据申请人用户ID查找申请
     */
    List<EmployeeApplication> findByApplicantUser_IdOrderByCreatedAtDesc(Long applicantUserId);
    
    /**
     * 根据状态查找申请
     */
    Page<EmployeeApplication> findByStatusOrderByCreatedAtDesc(ApplicationStatus status, Pageable pageable);
    
    /**
     * 根据状态列表查找申请
     */
    Page<EmployeeApplication> findByStatusInOrderByCreatedAtDesc(List<ApplicationStatus> statuses, Pageable pageable);
    
    /**
     * 查找待审核的申请（提交状态和需要补充资料状态）
     */
    @Query("SELECT ea FROM EmployeeApplication ea WHERE ea.status IN ('PENDING', 'UNDER_REVIEW') ORDER BY ea.createdAt DESC")
    Page<EmployeeApplication> findPendingApplications(Pageable pageable);
    
    /**
     * 根据审核人ID查找申请
     */
    List<EmployeeApplication> findByReviewerUser_IdOrderByReviewedAtDesc(Long reviewerUserId);
    
    /**
     * 根据身份证号查找申请
     */
    Optional<EmployeeApplication> findByIdNumber(String idNumber);
    
    /**
     * 根据邮箱查找申请
     */
    List<EmployeeApplication> findByEmailOrderByCreatedAtDesc(String email);
    
    /**
     * 统计指定时间范围内的申请数量
     */
    @Query("SELECT COUNT(ea) FROM EmployeeApplication ea WHERE ea.createdAt BETWEEN :startDate AND :endDate")
    long countApplicationsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    /**
     * 根据状态统计申请数量
     */
    long countByStatus(ApplicationStatus status);
    
    /**
     * 检查身份证号是否已存在
     */
    boolean existsByIdNumber(String idNumber);
    
    /**
     * 检查邮箱在指定状态下是否已存在申请
     */
    boolean existsByEmailAndStatusIn(String email, List<ApplicationStatus> statuses);

    /**
     * 查找指定状态且在指定时间之前提交的申请（用于超时检查）
     */
    List<EmployeeApplication> findByStatusInAndSubmittedAtBefore(List<ApplicationStatus> statuses, LocalDateTime submittedBefore);
}