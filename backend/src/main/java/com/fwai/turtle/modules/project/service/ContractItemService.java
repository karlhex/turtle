package com.fwai.turtle.modules.project.service;

import com.fwai.turtle.modules.project.dto.ContractItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContractItemService {
    ContractItemDTO create(ContractItemDTO contractItemDTO);
    
    ContractItemDTO update(Long id, ContractItemDTO contractItemDTO);
    
    void delete(Long id);
    
    ContractItemDTO getById(Long id);
    
    List<ContractItemDTO> getByContractId(Long contractId);
    
    Page<ContractItemDTO> search(
            Long contractId,
            Long productId,
            String modelNumber,
            Pageable pageable
    );
    
    void deleteByContractId(Long contractId);
}
