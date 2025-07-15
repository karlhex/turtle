package com.fwai.turtle.modules.project.service;

import com.fwai.turtle.modules.project.dto.ContractDownPaymentDTO;
import com.fwai.turtle.base.types.DebitCreditType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ContractDownPaymentService {
    ContractDownPaymentDTO create(ContractDownPaymentDTO paymentDTO);
    
    ContractDownPaymentDTO update(Long id, ContractDownPaymentDTO paymentDTO);
    
    void delete(Long id);
    
    ContractDownPaymentDTO getById(Long id);
    
    List<ContractDownPaymentDTO> getByContractId(Long contractId);
    
    Page<ContractDownPaymentDTO> search(
            Long contractId,
            DebitCreditType debitCreditType,
            LocalDate startDate,
            LocalDate endDate,
            Boolean paymentStatus,
            Pageable pageable
    );
    
    List<ContractDownPaymentDTO> getOverduePayments(LocalDate date);
    
    ContractDownPaymentDTO confirmPayment(Long id, LocalDate actualDate);
    
    void deleteByContractId(Long contractId);
}
