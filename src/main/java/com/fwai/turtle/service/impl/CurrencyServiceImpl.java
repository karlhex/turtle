package com.fwai.turtle.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fwai.turtle.dto.CurrencyDTO;
import com.fwai.turtle.exception.DuplicateRecordException;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Currency;
import com.fwai.turtle.persistence.mapper.CurrencyMapper;
import com.fwai.turtle.persistence.repository.CurrencyRepository;
import com.fwai.turtle.service.interfaces.CurrencyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    @Override
    @Transactional
    public CurrencyDTO create(CurrencyDTO currencyDTO) {
        if (currencyRepository.existsByCode(currencyDTO.getCode())) {
            throw new DuplicateRecordException("货币代码已存在");
        }

        Currency currency = currencyMapper.toEntity(currencyDTO);
        
        // If this is the first currency, make it the base currency
        if (currencyRepository.count() == 0) {
            currency.setIsBaseCurrency(true);
            currency.setExchangeRate(1.0);
        }
        
        currency = currencyRepository.save(currency);
        return currencyMapper.toDTO(currency);
    }

    @Override
    @Transactional
    public CurrencyDTO update(Long id, CurrencyDTO currencyDTO) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("货币不存在"));

        // Check if currency code is being changed and if it already exists
        if (!currency.getCode().equals(currencyDTO.getCode()) &&
                currencyRepository.existsByCode(currencyDTO.getCode())) {
            throw new DuplicateRecordException("货币代码已存在");
        }

        // Prevent changing isBaseCurrency through update
        currencyDTO.setIsBaseCurrency(currency.getIsBaseCurrency());
        
        currencyMapper.updateEntity(currencyDTO, currency);
        currency = currencyRepository.save(currency);
        return currencyMapper.toDTO(currency);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("货币不存在"));
                
        if (currency.getIsBaseCurrency()) {
            throw new IllegalStateException("不能删除基准货币");
        }
        
        currencyRepository.deleteById(id);
    }

    @Override
    public CurrencyDTO getById(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("货币不存在"));
        return currencyMapper.toDTO(currency);
    }

    @Override
    public CurrencyDTO getByCode(String code) {
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("货币不存在"));
        return currencyMapper.toDTO(currency);
    }

    @Override
    public CurrencyDTO getBaseCurrency() {
        Currency currency = currencyRepository.findByIsBaseCurrency(true)
                .orElseThrow(() -> new ResourceNotFoundException("基准货币不存在"));
        return currencyMapper.toDTO(currency);
    }

    @Override
    public Page<CurrencyDTO> getAll(Pageable pageable) {
        return currencyRepository.findAll(pageable).map(currencyMapper::toDTO);
    }

    @Override
    public Page<CurrencyDTO> getAllActive(Pageable pageable) {
        return currencyRepository.findByActive(true, pageable).map(currencyMapper::toDTO);
    }

    @Override
    public Page<CurrencyDTO> search(String query, Pageable pageable) {
        if (!StringUtils.hasText(query)) {
            return getAll(pageable);
        }
        
        return currencyRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
                query, query, pageable)
                .map(currencyMapper::toDTO);
    }

    @Override
    @Transactional
    public void setBaseCurrency(Long id) {
        Currency newBaseCurrency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("货币不存在"));
                
        Currency currentBaseCurrency = currencyRepository.findByIsBaseCurrency(true)
                .orElse(null);
                
        if (currentBaseCurrency != null) {
            currentBaseCurrency.setIsBaseCurrency(false);
            currencyRepository.save(currentBaseCurrency);
        }
        
        newBaseCurrency.setIsBaseCurrency(true);
        newBaseCurrency.setExchangeRate(1.0);
        currencyRepository.save(newBaseCurrency);
    }

    @Override
    @Transactional
    public void updateExchangeRate(String code, Double rate) {
        Currency currency = currencyRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("货币不存在"));
                
        if (currency.getIsBaseCurrency()) {
            throw new IllegalStateException("不能更新基准货币的汇率");
        }
        
        currency.setExchangeRate(rate);
        currencyRepository.save(currency);
    }
}
