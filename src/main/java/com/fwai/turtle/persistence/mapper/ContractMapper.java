package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.ContractDTO;
import com.fwai.turtle.persistence.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    componentModel = "spring",
    uses = {
        ContractItemMapper.class,
        ContractDownPaymentMapper.class,
        CurrencyMapper.class,
        CompanyMapper.class
    }
)
public interface ContractMapper {
    @Mapping(source = "currency", target = "currency")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "downPayments", target = "downPayments")
    @Mapping(source = "projectId", target = "projectId")
    @Mapping(source = "buyerCompany", target = "buyerCompany")
    @Mapping(source = "sellerCompany", target = "sellerCompany")
    ContractDTO toDTO(Contract contract);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "downPayments", ignore = true)
    @Mapping(target = "buyerCompany", ignore = false)
    @Mapping(target = "sellerCompany", ignore = false)
    Contract toEntity(ContractDTO dto);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "downPayments", ignore = true)
    @Mapping(target = "buyerCompany", ignore = false)
    @Mapping(target = "sellerCompany", ignore = false)
    void updateEntityFromDTO(ContractDTO dto, @MappingTarget Contract contract);
}
