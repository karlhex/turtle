package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.ContractDownPaymentDTO;
import com.fwai.turtle.persistence.entity.ContractDownPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContractDownPaymentMapper {
    ContractDownPaymentMapper INSTANCE = Mappers.getMapper(ContractDownPaymentMapper.class);

    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "contract.contractNo", target = "contractNo")
    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.code", target = "currencyCode")
    ContractDownPaymentDTO toDTO(ContractDownPayment payment);

    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "currency", ignore = true)
    ContractDownPayment toEntity(ContractDownPaymentDTO dto);
}
