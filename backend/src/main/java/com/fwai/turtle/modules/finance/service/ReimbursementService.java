package com.fwai.turtle.modules.finance.service;

import com.fwai.turtle.modules.finance.dto.ReimbursementDTO;
import com.fwai.turtle.modules.finance.dto.ReimbursementItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReimbursementService {
    Page<ReimbursementDTO> findAll(Pageable pageable);
    
    ReimbursementDTO getById(Long id);
    
    ReimbursementDTO getByReimbursementNo(String reimbursementNo);
    
    ReimbursementDTO create(ReimbursementDTO reimbursementDTO);
    
    ReimbursementDTO update(Long id, ReimbursementDTO reimbursementDTO);
    
    ReimbursementDTO apply(Long id);
    
    ReimbursementDTO approve(Long id);
    
    ReimbursementDTO reject(Long id, String reason);
    
    void delete(Long id);

    List<ReimbursementDTO> getByApplicant(Long applicantId);
    
    List<ReimbursementDTO> getByApprover(Long approverId);
    
    List<ReimbursementDTO> getByProject(Long projectId);
    
    Page<ReimbursementDTO> getByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    ReimbursementDTO addItem(Long reimbursementId, ReimbursementItemDTO itemDTO);
    
    ReimbursementDTO updateItem(Long reimbursementId, Long itemId, ReimbursementItemDTO itemDTO);
    
    void removeItem(Long reimbursementId, Long itemId);
    
    List<ReimbursementItemDTO> getItems(Long reimbursementId);
}
