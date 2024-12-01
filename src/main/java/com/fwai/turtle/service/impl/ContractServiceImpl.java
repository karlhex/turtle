package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.ContractDTO;
import com.fwai.turtle.dto.ContractItemDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Contract;
import com.fwai.turtle.persistence.entity.ContractItem;
import com.fwai.turtle.persistence.entity.ContractStatus;
import com.fwai.turtle.persistence.entity.ContractType;
import com.fwai.turtle.persistence.entity.Currency;
import com.fwai.turtle.persistence.entity.Product;
import com.fwai.turtle.persistence.mapper.ContractItemMapper;
import com.fwai.turtle.persistence.mapper.ContractMapper;
import com.fwai.turtle.persistence.repository.ContractRepository;
import com.fwai.turtle.persistence.repository.CurrencyRepository;
import com.fwai.turtle.persistence.repository.ProductRepository;
import com.fwai.turtle.service.interfaces.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final CurrencyRepository currencyRepository;
    private final ContractMapper contractMapper;
    private final ContractItemMapper contractItemMapper;
    private final ProductRepository productRepository;

    @Override
    public Page<ContractDTO> findAll(Pageable pageable) {
        return contractRepository.findAll(pageable).map(contractMapper::toDTO);
    }

    @Override
    public ContractDTO create(ContractDTO contractDTO) {
        if (contractRepository.existsByContractNo(contractDTO.getContractNo())) {
            throw new IllegalArgumentException("合同编号已存在: " + contractDTO.getContractNo());
        }

        Contract contract = contractMapper.toEntity(contractDTO);
        
        // 设置币种
        Currency currency = currencyRepository.findById(contractDTO.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", contractDTO.getCurrencyId()));
        contract.setCurrency(currency);

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO update(Long id, ContractDTO contractDTO) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", id));

        // 检查合同编号是否被其他合同使用
        if (!existingContract.getContractNo().equals(contractDTO.getContractNo()) &&
            contractRepository.existsByContractNo(contractDTO.getContractNo())) {
            throw new IllegalArgumentException("合同编号已存在: " + contractDTO.getContractNo());
        }

        Contract contract = contractMapper.toEntity(contractDTO);
        contract.setId(id);

        // 设置币种
        Currency currency = currencyRepository.findById(contractDTO.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", contractDTO.getCurrencyId()));
        contract.setCurrency(currency);

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public void delete(Long id) {
        if (!contractRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contract", "id", id);
        }
        contractRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractDTO getById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", id));
        return contractMapper.toDTO(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractDTO getByContractNo(String contractNo) {
        Contract contract = contractRepository.findByContractNo(contractNo)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "contractNo", contractNo));
        return contractMapper.toDTO(contract);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDTO> search(
            String contractNo,
            String title,
            String company,
            ContractType type,
            ContractStatus status,
            String projectNo,
            Pageable pageable
    ) {
        return contractRepository.search(contractNo, title, company, type, status, projectNo, pageable)
                .map(contractMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDTO> getByStatus(ContractStatus status) {
        return contractRepository.findByStatus(status).stream()
                .map(contractMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDTO> getExpiredContracts(LocalDate date) {
        return contractRepository.findByEndDateBefore(date).stream()
                .map(contractMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDTO> getContractsByDateRange(LocalDate startDate, LocalDate endDate) {
        return contractRepository.findByStartDateBetween(startDate, endDate).stream()
                .map(contractMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDTO updateStatus(Long id, ContractStatus newStatus) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", id));
        contract.setStatus(newStatus);
        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO addContractItem(Long contractId, ContractItemDTO itemDTO) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractId));

        ContractItem item = contractItemMapper.toEntity(itemDTO);
        
        // 设置产品
        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProductId()));
        item.setProduct(product);
        
        // 设置合同关联
        item.setContract(contract);
        contract.getItems().add(item);
        
        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO updateContractItem(Long contractId, Long itemId, ContractItemDTO itemDTO) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractId));

        ContractItem existingItem = contract.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ContractItem", "id", itemId));

        // 更新项目信息
        ContractItem updatedItem = contractItemMapper.toEntity(itemDTO);
        updatedItem.setId(itemId);
        updatedItem.setContract(contract);

        // 设置产品
        Product product = productRepository.findById(itemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProductId()));
        updatedItem.setProduct(product);

        // 替换原有项目
        contract.getItems().remove(existingItem);
        contract.getItems().add(updatedItem);

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO removeContractItem(Long contractId, Long itemId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractId));

        boolean removed = contract.getItems().removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            throw new ResourceNotFoundException("ContractItem", "id", itemId);
        }

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public List<ContractItemDTO> getContractItems(Long contractId) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractId));

        return contract.getItems().stream()
                .map(contractItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDTO updateContractItems(Long contractId, List<ContractItemDTO> items) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", contractId));

        // 清除原有项目
        contract.getItems().clear();

        // 添加新项目
        for (ContractItemDTO itemDTO : items) {
            ContractItem item = contractItemMapper.toEntity(itemDTO);
            item.setContract(contract);

            // 设置产品
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProductId()));
            item.setProduct(product);

            contract.getItems().add(item);
        }

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }
}
