package com.fwai.turtle.modules.project.service.impl;

import com.fwai.turtle.modules.project.dto.ContractItemDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.project.entity.Contract;
import com.fwai.turtle.modules.project.entity.ContractItem;
import com.fwai.turtle.modules.customer.entity.Product;
import com.fwai.turtle.modules.project.mapper.ContractItemMapper;
import com.fwai.turtle.modules.project.repository.ContractItemRepository;
import com.fwai.turtle.modules.project.repository.ContractRepository;
import com.fwai.turtle.modules.customer.repository.ProductRepository;
import com.fwai.turtle.modules.project.service.ContractItemService;
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
        Product product = productRepository.findById(contractItemDTO.getProduct().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", contractItemDTO.getProduct().getId()));
        contractItem.setProduct(product);

        contractItem = contractItemRepository.save(contractItem);
        return contractItemMapper.toDTO(contractItem);
    }

    @Override
    public ContractItemDTO update(Long id, ContractItemDTO contractItemDTO) {
        ContractItem existingItem = contractItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractItem", "id", id));

        // 更新基本信息
        contractItemMapper.toEntity(contractItemDTO);
        
        // 更新产品信息（如果有变化）
        if (!existingItem.getProduct().getId().equals(contractItemDTO.getProduct().getId())) {
            Product product = productRepository.findById(contractItemDTO.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", contractItemDTO.getProduct().getId()));
            existingItem.setProduct(product);
        }

        existingItem = contractItemRepository.save(existingItem);
        return contractItemMapper.toDTO(existingItem);
    }

    @Override
    public void delete(Long id) {
        if (!contractItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("ContractItem", "id", id);
        }
        contractItemRepository.deleteById(id);
    }

    @Override
    public ContractItemDTO getById(Long id) {
        ContractItem contractItem = contractItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractItem", "id", id));
        return contractItemMapper.toDTO(contractItem);
    }

    @Override
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
