package com.fwai.turtle.modules.finance.repository;

import com.fwai.turtle.modules.finance.entity.ReimbursementItem;
import com.fwai.turtle.modules.finance.entity.Reimbursement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReimbursementItemRepository extends JpaRepository<ReimbursementItem, Long> {
    
    List<ReimbursementItem> findByReimbursement(Reimbursement reimbursement);
    
    List<ReimbursementItem> findByReimbursementId(Long reimbursementId);
    
    Page<ReimbursementItem> findByOccurrenceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    Page<ReimbursementItem> findByReasonContaining(String reason, Pageable pageable);
    
    @Query("SELECT i FROM ReimbursementItem i WHERE i.reimbursement.applicantId = :employeeId")
    List<ReimbursementItem> findByEmployeeId(@Param("employeeId") Long employeeId);
    
    @Query("SELECT i FROM ReimbursementItem i WHERE i.reimbursement.projectId = :projectId")
    List<ReimbursementItem> findByProjectId(@Param("projectId") Long projectId);
    
    void deleteByReimbursementId(Long reimbursementId);
}
