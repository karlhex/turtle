package com.fwai.turtle.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.fwai.turtle.dto.ProductDTO;
import com.fwai.turtle.persistence.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toEntity(ProductDTO productDTO);
    void updateEntity(ProductDTO productDTO, @MappingTarget Product product);
}
