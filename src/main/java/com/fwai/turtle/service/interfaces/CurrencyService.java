package com.fwai.turtle.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fwai.turtle.dto.CurrencyDTO;

public interface CurrencyService {
    CurrencyDTO create(CurrencyDTO currencyDTO);
    CurrencyDTO update(Long id, CurrencyDTO currencyDTO);
    void delete(Long id);
    CurrencyDTO getById(Long id);
    CurrencyDTO getByCode(String code);
    CurrencyDTO getBaseCurrency();
    Page<CurrencyDTO> getAll(Pageable pageable);
    Page<CurrencyDTO> getAllActive(Pageable pageable);
    Page<CurrencyDTO> search(String query, Pageable pageable);
    void setBaseCurrency(Long id);
    void updateExchangeRate(String code, Double rate);
}
