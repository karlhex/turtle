package com.fwai.turtle.modules.finance.mapper;

import com.fwai.turtle.modules.finance.dto.InvoiceDTO;
import com.fwai.turtle.modules.finance.entity.Invoice;
import org.mapstruct.*;
import java.util.List;

import com.fwai.turtle.modules.finance.mapper.TaxInfoMapper;
import com.fwai.turtle.modules.project.mapper.ContractMapper;


@Mapper(componentModel = "spring", uses = {TaxInfoMapper.class,ContractMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {
    
    InvoiceDTO toDTO(Invoice invoice);
    
    List<InvoiceDTO> toDTOs(List<Invoice> invoices);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Invoice toEntity(InvoiceDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(InvoiceDTO dto, @MappingTarget Invoice invoice);
}
