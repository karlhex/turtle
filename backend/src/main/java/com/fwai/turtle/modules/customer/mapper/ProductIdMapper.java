package com.fwai.turtle.modules.customer.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.modules.customer.entity.Product;
import com.fwai.turtle.modules.customer.repository.ProductRepository;

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
