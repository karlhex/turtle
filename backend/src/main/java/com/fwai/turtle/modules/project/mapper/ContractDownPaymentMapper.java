package com.fwai.turtle.modules.project.mapper;

import com.fwai.turtle.modules.project.dto.ContractDownPaymentDTO;
import com.fwai.turtle.modules.project.entity.ContractDownPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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

    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "currency", ignore = true)
    void updateEntityFromDTO(ContractDownPaymentDTO dto, @MappingTarget ContractDownPayment entity);
}
