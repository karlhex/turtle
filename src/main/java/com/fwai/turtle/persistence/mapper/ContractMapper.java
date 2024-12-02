package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.ContractDTO;
import com.fwai.turtle.persistence.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "spring",
    uses = {
        ContractItemMapper.class,
        ContractDownPaymentMapper.class
    }
)
public interface ContractMapper {
    ContractMapper INSTANCE = Mappers.getMapper(ContractMapper.class);

    @Mapping(source = "currency.id", target = "currencyId")
    @Mapping(source = "currency.code", target = "currencyCode")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "downPayments", target = "downPayments")
    @Mapping(source = "project.id", target = "projectId")
    ContractDTO toDTO(Contract contract);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "downPayments", ignore = true)
    @Mapping(target = "project", ignore = true)
    Contract toEntity(ContractDTO dto);
}
