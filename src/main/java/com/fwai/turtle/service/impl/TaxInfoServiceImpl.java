package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.TaxInfoDTO;
import com.fwai.turtle.exception.DuplicateRecordException;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.TaxInfo;
import com.fwai.turtle.persistence.mapper.TaxInfoMapper;
import com.fwai.turtle.persistence.repository.TaxInfoRepository;
import com.fwai.turtle.service.interfaces.TaxInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaxInfoServiceImpl implements TaxInfoService {

    private final TaxInfoRepository taxInfoRepository;
    private final TaxInfoMapper taxInfoMapper;

    @Override
    public Page<TaxInfoDTO> getTaxInfos(Pageable pageable, Boolean active) {
        Page<TaxInfo> page;
        if (active != null) {
            page = taxInfoRepository.findByActive(active, pageable);
        } else {
            page = taxInfoRepository.findAll(pageable);
        }
        return page.map(taxInfoMapper::toDTO);
    }

    @Override
    public TaxInfoDTO getTaxInfo(Long id) {
        return taxInfoRepository.findById(id)
                .map(taxInfoMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Tax info not found with id: " + id));
    }

    @Override
    @Transactional
    public TaxInfoDTO createTaxInfo(TaxInfoDTO taxInfoDTO) {
        // 验证税号是否已存在
        if (taxInfoRepository.existsByTaxNo(taxInfoDTO.getTaxNo())) {
            throw new DuplicateRecordException("税号已存在: " + taxInfoDTO.getTaxNo());
        }

        // 验证银行账号是否已存在
        if (taxInfoRepository.existsByBankAccount(taxInfoDTO.getBankAccount())) {
            throw new DuplicateRecordException("银行账号已存在: " + taxInfoDTO.getBankAccount());
        }

        TaxInfo taxInfo = taxInfoMapper.toEntity(taxInfoDTO);
        taxInfo.setActive(true);
        taxInfo.setCreatedAt(LocalDateTime.now());
        taxInfo.setUpdatedAt(LocalDateTime.now());
        
        taxInfo = taxInfoRepository.save(taxInfo);
        return taxInfoMapper.toDTO(taxInfo);
    }

    @Override
    @Transactional
    public TaxInfoDTO updateTaxInfo(Long id, TaxInfoDTO taxInfoDTO) {
        TaxInfo existingTaxInfo = taxInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax info not found with id: " + id));

        // 验证税号是否被其他记录使用
        if (!existingTaxInfo.getTaxNo().equals(taxInfoDTO.getTaxNo()) &&
            taxInfoRepository.existsByTaxNo(taxInfoDTO.getTaxNo())) {
            throw new DuplicateRecordException("税号已存在: " + taxInfoDTO.getTaxNo());
        }

        // 验证银行账号是否被其他记录使用
        if (!existingTaxInfo.getBankAccount().equals(taxInfoDTO.getBankAccount()) &&
            taxInfoRepository.existsByBankAccount(taxInfoDTO.getBankAccount())) {
            throw new DuplicateRecordException("银行账号已存在: " + taxInfoDTO.getBankAccount());
        }

        taxInfoMapper.updateEntityFromDTO(taxInfoDTO, existingTaxInfo);
        existingTaxInfo.setUpdatedAt(LocalDateTime.now());
        existingTaxInfo = taxInfoRepository.save(existingTaxInfo);
        return taxInfoMapper.toDTO(existingTaxInfo);
    }

    @Override
    @Transactional
    public void deleteTaxInfo(Long id) {
        TaxInfo taxInfo = taxInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax info not found with id: " + id));
        taxInfoRepository.delete(taxInfo);
    }

    @Override
    @Transactional
    public TaxInfoDTO toggleStatus(Long id) {
        TaxInfo taxInfo = taxInfoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tax info not found with id: " + id));
        
        taxInfo.setActive(!taxInfo.getActive());
        taxInfo.setUpdatedAt(LocalDateTime.now());
        taxInfo = taxInfoRepository.save(taxInfo);
        return taxInfoMapper.toDTO(taxInfo);
    }

    @Override
    public Page<TaxInfoDTO> searchTaxInfos(String query, Pageable pageable) {
        return taxInfoRepository.search(query, pageable)
                .map(taxInfoMapper::toDTO);
    }
}
