package com.fwai.turtle.modules.finance.service;

import com.fwai.turtle.modules.finance.dto.ReimbursementItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReimbursementItemService {
    Page<ReimbursementItemDTO> findAll(Pageable pageable);
    
    ReimbursementItemDTO getById(Long id);
    
    ReimbursementItemDTO create(ReimbursementItemDTO itemDTO);
    
    ReimbursementItemDTO update(Long id, ReimbursementItemDTO itemDTO);
    
    void delete(Long id);
    
    List<ReimbursementItemDTO> getByReimbursement(Long reimbursementId);
    
    List<ReimbursementItemDTO> getByDateRange(LocalDate startDate, LocalDate endDate);
    
    Page<ReimbursementItemDTO> searchByReason(String reason, Pageable pageable);
}
