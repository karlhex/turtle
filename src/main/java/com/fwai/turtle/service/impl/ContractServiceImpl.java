package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.ContractDTO;
import com.fwai.turtle.dto.ContractItemDTO;
import com.fwai.turtle.dto.CurrencyDTO;
import com.fwai.turtle.dto.ProductDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Contract;
import com.fwai.turtle.persistence.entity.ContractItem;
import com.fwai.turtle.persistence.entity.Currency;
import com.fwai.turtle.persistence.entity.Product;
import com.fwai.turtle.persistence.mapper.ContractItemMapper;
import com.fwai.turtle.persistence.mapper.ContractMapper;
import com.fwai.turtle.persistence.repository.ContractRepository;
import com.fwai.turtle.persistence.repository.CompanyRepository;
import com.fwai.turtle.persistence.repository.CurrencyRepository;
import com.fwai.turtle.persistence.repository.ProductRepository;
import com.fwai.turtle.service.interfaces.ContractService;
import com.fwai.turtle.types.ContractStatus;
import com.fwai.turtle.types.ContractType;
import com.fwai.turtle.dto.CompanyDTO;

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
    private final CompanyRepository companyRepository;
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


        // Validate buyer company exists
        companyRepository.findById(contractDTO.getBuyerCompany().getId())
            .orElseThrow(() -> new ResourceNotFoundException("买方公司不存在"));

        // Validate seller company exists
        companyRepository.findById(contractDTO.getSellerCompany().getId())
            .orElseThrow(() -> new ResourceNotFoundException("卖方公司不存在"));

        Contract contract = contractMapper.toEntity(contractDTO);
        
        // 设置币种
        Currency currency = currencyRepository.findById(contractDTO.getCurrency().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", contractDTO.getCurrency().getId()));
        contract.setCurrency(currency);

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }

    @Override
    public ContractDTO update(Long id, ContractDTO contractDTO) {
        Contract existingContract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", id));

        // 检查合同编号是否已存在（如果有变化）
        if (!existingContract.getContractNo().equals(contractDTO.getContractNo()) &&
            contractRepository.existsByContractNo(contractDTO.getContractNo())) {
            throw new IllegalArgumentException("合同编号已存在: " + contractDTO.getContractNo());
        }

        // 更新合同基本信息
        contractMapper.updateEntityFromDTO(contractDTO, existingContract);

        // 更新关联的公司信息
        CompanyDTO buyerCompany = contractDTO.getBuyerCompany();
        if (buyerCompany != null) {
            existingContract.setBuyerCompany(companyRepository.findById(buyerCompany.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", buyerCompany.getId())));
        }
        CompanyDTO sellerCompany = contractDTO.getSellerCompany();
        if (sellerCompany != null) {
            existingContract.setSellerCompany(companyRepository.findById(sellerCompany.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company", "id", sellerCompany.getId())));
        }


        // 更新货币信息
        CurrencyDTO currency = contractDTO.getCurrency();
        if (currency!= null) {
            existingContract.setCurrency(currencyRepository.findById(currency.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", currency.getId())));
        }

        // 更新合同明细
        if (contractDTO.getItems() != null) {
            // Clear existing items and add new ones
            existingContract.getItems().clear();
            
            List<ContractItem> updatedItems = contractDTO.getItems().stream()
                    .map(itemDTO -> {
                        ContractItem item = contractItemMapper.toEntity(itemDTO);
                        item.setContract(existingContract);
                        
                        // 设置产品
                        if (itemDTO.getProduct() != null && itemDTO.getProduct().getId() != null) {
                            Product product = productRepository.findById(itemDTO.getProduct().getId())
                                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProduct().getId()));
                            item.setProduct(product);
                        }
                        return item;
                    })
                    .collect(Collectors.toList());
            
            existingContract.getItems().addAll(updatedItems);
        }

        Contract savedContract = contractRepository.save(existingContract);
        return contractMapper.toDTO(savedContract);
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
        if (itemDTO.getProduct() != null && itemDTO.getProduct().getId() != null) {
            Product product = productRepository.findById(itemDTO.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProduct().getId()));
            item.setProduct(product);
        }
        
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
        if (itemDTO.getProduct() != null && itemDTO.getProduct().getId() != null) {
            Product product = productRepository.findById(itemDTO.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProduct().getId()));
            updatedItem.setProduct(product);
        }

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
            if (itemDTO.getProduct() != null && itemDTO.getProduct().getId() != null) {
                Product product = productRepository.findById(itemDTO.getProduct().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product", "id", itemDTO.getProduct().getId()));
                item.setProduct(product);
            }

            contract.getItems().add(item);
        }

        contract = contractRepository.save(contract);
        return contractMapper.toDTO(contract);
    }
}
