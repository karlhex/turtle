package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.InvoiceDTO;
import com.fwai.turtle.persistence.entity.Invoice;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", uses = {TaxInfoMapper.class})
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
