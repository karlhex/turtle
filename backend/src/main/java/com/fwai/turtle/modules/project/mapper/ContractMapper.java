package com.fwai.turtle.modules.project.mapper;

import com.fwai.turtle.modules.project.dto.ContractDTO;
import com.fwai.turtle.modules.project.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.fwai.turtle.modules.finance.mapper.CurrencyMapper;
import com.fwai.turtle.modules.customer.mapper.CompanyMapper;
import com.fwai.turtle.modules.finance.mapper.InvoiceMapper;
import com.fwai.turtle.modules.customer.mapper.ContactMapper;

@Mapper(
    componentModel = "spring",
    uses = {
        ContractItemMapper.class,
        ContractDownPaymentMapper.class,
        CurrencyMapper.class,
        CompanyMapper.class,
        InvoiceMapper.class,
        ContactMapper.class
    }
)
public interface ContractMapper {
    ContractDTO toDTO(Contract contract);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "downPayments", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "buyerCompany", ignore = false)
    @Mapping(target = "sellerCompany", ignore = false)
    @Mapping(target = "contactPerson", ignore = false)
    Contract toEntity(ContractDTO dto);

    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "downPayments", ignore = true)
    @Mapping(target = "invoices", ignore = true)
    @Mapping(target = "buyerCompany", ignore = false)
    @Mapping(target = "sellerCompany", ignore = false)
    @Mapping(target = "contactPerson", ignore = false)
    void updateEntityFromDTO(ContractDTO dto, @MappingTarget Contract contract);
}
