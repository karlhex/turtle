package com.fwai.turtle.modules.project.service;

import com.fwai.turtle.modules.project.dto.ContractDTO;
import com.fwai.turtle.modules.project.dto.ContractItemDTO;
import com.fwai.turtle.base.types.ContractStatus;
import com.fwai.turtle.base.types.ContractType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ContractService {
    ContractDTO create(ContractDTO contractDTO);
    
    ContractDTO update(Long id, ContractDTO contractDTO);
    
    void delete(Long id);
    
    ContractDTO getById(Long id);
    
    ContractDTO getByContractNo(String contractNo);
    
    Page<ContractDTO> findAll(Pageable pageable);
    
    Page<ContractDTO> search(
            String contractNo,
            String title,
            String company,
            ContractType type,
            ContractStatus status,
            String projectNo,
            Pageable pageable
    );
    
    List<ContractDTO> getByStatus(ContractStatus status);
    
    List<ContractDTO> getExpiredContracts(LocalDate date);
    
    List<ContractDTO> getContractsByDateRange(LocalDate startDate, LocalDate endDate);
    
    ContractDTO updateStatus(Long id, ContractStatus newStatus);

    List<ContractDTO> getContractsByInvoiceNo(String invoiceNo);

    // Contract Items management
    ContractDTO addContractItem(Long contractId, ContractItemDTO itemDTO);
    
    ContractDTO updateContractItem(Long contractId, Long itemId, ContractItemDTO itemDTO);
    
    ContractDTO removeContractItem(Long contractId, Long itemId);

    // Invoice management
    ContractDTO getContractByInvoiceId(Long invoiceId);
    
    List<ContractItemDTO> getContractItems(Long contractId);
    
    ContractDTO updateContractItems(Long contractId, List<ContractItemDTO> items);
}
