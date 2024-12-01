package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.ContractItemDTO;
import com.fwai.turtle.persistence.entity.ContractItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContractItemMapper {
    ContractItemMapper INSTANCE = Mappers.getMapper(ContractItemMapper.class);

    @Mapping(source = "contract.id", target = "contractId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    ContractItemDTO toDTO(ContractItem contractItem);

    @Mapping(target = "contract", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    ContractItem toEntity(ContractItemDTO dto);
}
