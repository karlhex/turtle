package com.fwai.turtle.service;

import com.fwai.turtle.dto.CurrencyDTO;
import com.fwai.turtle.persistence.entity.Currency;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.mapper.CurrencyMapper;
import com.fwai.turtle.persistence.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;

    /**
     * 获取货币列表（分页）
     *
     * @param pageable 分页参数
     * @param active 是否启用
     * @return 分页货币列表
     */
    public Page<CurrencyDTO> getCurrencies(Pageable pageable, Boolean active) {
        Page<Currency> page;
        if (active != null) {
            page = currencyRepository.findByActive(active, pageable);
        } else {
            page = currencyRepository.findAll(pageable);
        }
        return page.map(currencyMapper::toDTO);
    }

    /**
     * 根据ID获取货币
     *
     * @param id 货币ID
     * @return 货币信息
     */
    public CurrencyDTO getCurrency(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        return currencyMapper.toDTO(currency);
    }

    /**
     * 创建货币
     *
     * @param currencyDTO 货币信息
     * @return 创建的货币信息
     */
    @Transactional
    public CurrencyDTO createCurrency(CurrencyDTO currencyDTO) {
        Currency currency = currencyMapper.toEntity(currencyDTO);
        currency.setActive(true);
        currency.setCreatedAt(LocalDateTime.now());
        currency.setUpdatedAt(LocalDateTime.now());
        
        // 如果设置为基准货币，需要将其他货币的基准货币标志设为false
        if (Boolean.TRUE.equals(currency.getIsBaseCurrency())) {
            currencyRepository.clearBaseCurrency();
        }
        
        currency = currencyRepository.save(currency);
        return currencyMapper.toDTO(currency);
    }

    /**
     * 更新货币
     *
     * @param currencyDTO 货币信息
     * @return 更新后的货币信息
     */
    @Transactional
    public CurrencyDTO updateCurrency(CurrencyDTO currencyDTO) {
        Currency currency = currencyRepository.findById(currencyDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + currencyDTO.getId()));

        // 如果设置为基准货币，需要将其他货币的基准货币标志设为false
        if (Boolean.TRUE.equals(currencyDTO.getIsBaseCurrency()) && !Boolean.TRUE.equals(currency.getIsBaseCurrency())) {
            currencyRepository.clearBaseCurrency();
        }

        currencyMapper.updateEntity(currencyDTO, currency);
        currency.setUpdatedAt(LocalDateTime.now());
        currency = currencyRepository.save(currency);
        return currencyMapper.toDTO(currency);
    }

    /**
     * 删除货币
     *
     * @param id 货币ID
     */
    @Transactional
    public void deleteCurrency(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        
        // 不允许删除基准货币
        if (Boolean.TRUE.equals(currency.getIsBaseCurrency())) {
            throw new IllegalStateException("Cannot delete base currency");
        }
        
        currencyRepository.delete(currency);
    }

    /**
     * 切换货币状态
     *
     * @param id 货币ID
     * @return 更新后的货币信息
     */
    @Transactional
    public CurrencyDTO toggleStatus(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        
        // 不允许禁用基准货币
        if (Boolean.TRUE.equals(currency.getIsBaseCurrency()) && Boolean.TRUE.equals(currency.getActive())) {
            throw new IllegalStateException("Cannot deactivate base currency");
        }
        
        currency.setActive(!currency.getActive());
        currency.setUpdatedAt(LocalDateTime.now());
        currency = currencyRepository.save(currency);
        return currencyMapper.toDTO(currency);
    }

    /**
     * 设置为基准货币
     *
     * @param id 货币ID
     * @return 更新后的货币信息
     */
    @Transactional
    public CurrencyDTO setBaseCurrency(Long id) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currency not found with id: " + id));
        
        // 货币必须是启用状态才能设为基准货币
        if (!Boolean.TRUE.equals(currency.getActive())) {
            throw new IllegalStateException("Cannot set inactive currency as base currency");
        }
        
        currencyRepository.clearBaseCurrency();
        currency.setIsBaseCurrency(true);
        currency.setUpdatedAt(LocalDateTime.now());
        currency = currencyRepository.save(currency);
        return currencyMapper.toDTO(currency);
    }
}
