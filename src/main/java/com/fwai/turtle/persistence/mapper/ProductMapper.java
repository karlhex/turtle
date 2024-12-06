package com.fwai.turtle.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.fwai.turtle.dto.ProductDTO;
import com.fwai.turtle.persistence.entity.Product;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface ProductMapper {
    
    ProductDTO toDTO(Product entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductDTO dto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ProductDTO dto, @MappingTarget Product entity);
}
