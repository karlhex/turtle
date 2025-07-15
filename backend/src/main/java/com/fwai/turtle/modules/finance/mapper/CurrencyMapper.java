package com.fwai.turtle.modules.finance.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.fwai.turtle.modules.finance.dto.CurrencyDTO;
import com.fwai.turtle.modules.finance.entity.Currency;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyDTO toDTO(Currency currency);
    Currency toEntity(CurrencyDTO currencyDTO);
    void updateEntity(CurrencyDTO currencyDTO, @MappingTarget Currency currency);
}
