package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.ContractItemDTO;
import com.fwai.turtle.persistence.entity.ContractItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ContractItemMapper {
    ContractItemMapper INSTANCE = Mappers.getMapper(ContractItemMapper.class);

    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "product.name", target = "productName")
    ContractItemDTO toDTO(ContractItem contractItem);

    @Mapping(target = "contract", ignore = true)
    @Mapping(source = "product.id", target = "product.id")
    @Mapping(target = "totalAmount", ignore = true)
    ContractItem toEntity(ContractItemDTO dto);
}
