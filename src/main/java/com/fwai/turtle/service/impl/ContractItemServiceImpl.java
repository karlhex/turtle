package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.ContractItemDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Contract;
import com.fwai.turtle.persistence.entity.ContractItem;
import com.fwai.turtle.persistence.entity.Product;
import com.fwai.turtle.persistence.mapper.ContractItemMapper;
import com.fwai.turtle.persistence.repository.ContractItemRepository;
import com.fwai.turtle.persistence.repository.ContractRepository;
import com.fwai.turtle.persistence.repository.ProductRepository;
import com.fwai.turtle.service.interfaces.ContractItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractItemServiceImpl implements ContractItemService {
    private final ContractItemRepository contractItemRepository;
    private final ContractRepository contractRepository;
    private final ProductRepository productRepository;
    private final ContractItemMapper contractItemMapper;

    @Override
    public ContractItemDTO create(ContractItemDTO contractItemDTO) {
        ContractItem contractItem = contractItemMapper.toEntity(contractItemDTO);
        
        // 设置合同
        Contract contract = contractRepository.findById(contractItemDTO.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractItemDTO.getContractId()));
        contractItem.setContract(contract);
        
        // 设置产品
        Product product = productRepository.findById(contractItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", contractItemDTO.getProductId()));
        contractItem.setProduct(product);

        contractItem = contractItemRepository.save(contractItem);
        return contractItemMapper.toDTO(contractItem);
    }

    @Override
    public ContractItemDTO update(Long id, ContractItemDTO contractItemDTO) {
        ContractItem existingItem = contractItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractItem", "id", id));

        ContractItem contractItem = contractItemMapper.toEntity(contractItemDTO);
        contractItem.setId(id);
        contractItem.setContract(existingItem.getContract());  // 保持原有合同关联

        // 设置产品
        Product product = productRepository.findById(contractItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", contractItemDTO.getProductId()));
        contractItem.setProduct(product);

        contractItem = contractItemRepository.save(contractItem);
        return contractItemMapper.toDTO(contractItem);
    }

    @Override
    public void delete(Long id) {
        if (!contractItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("ContractItem", "id", id);
        }
        contractItemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractItemDTO getById(Long id) {
        ContractItem contractItem = contractItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractItem", "id", id));
        return contractItemMapper.toDTO(contractItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractItemDTO> getByContractId(Long contractId) {
        return contractItemRepository.findByContractId(contractId).stream()
                .map(contractItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractItemDTO> search(
            Long contractId,
            Long productId,
            String modelNumber,
            Pageable pageable
    ) {
        return contractItemRepository.search(contractId, productId, modelNumber, pageable)
                .map(contractItemMapper::toDTO);
    }

    @Override
    public void deleteByContractId(Long contractId) {
        contractItemRepository.deleteByContractId(contractId);
    }
}
