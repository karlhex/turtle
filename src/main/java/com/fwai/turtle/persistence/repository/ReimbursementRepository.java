package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Reimbursement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReimbursementRepository extends JpaRepository<Reimbursement, Long> {
    
    Optional<Reimbursement> findByReimbursementNo(String reimbursementNo);
    
    List<Reimbursement> findByApplicantId(Long applicantId);
    
    List<Reimbursement> findByApproverId(Long approverId);
    
    List<Reimbursement> findByProjectId(Long projectId);
    
    Page<Reimbursement> findByApplicationDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    @Query("SELECT r FROM Reimbursement r WHERE r.applicationDate <= :date AND r.approvalDate IS NULL")
    List<Reimbursement> findPendingApprovalBefore(@Param("date") LocalDate date);
    
    boolean existsByReimbursementNo(String reimbursementNo);
    
    @Query("SELECT r FROM Reimbursement r WHERE r.applicantId = :applicantId AND r.applicationDate BETWEEN :startDate AND :endDate")
    Page<Reimbursement> findByApplicantAndDateRange(
            @Param("applicantId") Long applicantId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable);
}
