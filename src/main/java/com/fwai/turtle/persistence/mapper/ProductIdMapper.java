package com.fwai.turtle.persistence.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.persistence.entity.Product;
import com.fwai.turtle.persistence.repository.ProductRepository;

@Component
public class ProductIdMapper {

    private final ProductRepository productRepository;

    public ProductIdMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product fromId(Long id) {
        if (id == null) return null;
        return productRepository.findById(id).orElse(null);
    }
}
