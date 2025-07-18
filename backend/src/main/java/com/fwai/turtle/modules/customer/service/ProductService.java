package com.fwai.turtle.modules.customer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fwai.turtle.modules.customer.dto.ProductDTO;
import com.fwai.turtle.base.types.ProductType;

public interface ProductService {
    ProductDTO create(ProductDTO productDTO);
    ProductDTO update(Long id, ProductDTO productDTO);
    void delete(Long id);
    ProductDTO getById(Long id);
    Page<ProductDTO> getAll(Pageable pageable);
    Page<ProductDTO> getAllActive(Pageable pageable);
    Page<ProductDTO> search(String query, Pageable pageable);
    Page<ProductDTO> getByType(ProductType type, Pageable pageable);
    void toggleStatus(Long id);
}
