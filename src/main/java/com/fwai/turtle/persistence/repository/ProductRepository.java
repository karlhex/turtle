package com.fwai.turtle.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.persistence.entity.Product;
import com.fwai.turtle.persistence.entity.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCaseOrModelNumberContainingIgnoreCaseOrManufacturerContainingIgnoreCase(
            String name, String modelNumber, String manufacturer, Pageable pageable);
            
    Page<Product> findByType(ProductType type, Pageable pageable);
    
    Page<Product> findByActive(Boolean active, Pageable pageable);
    
    boolean existsByNameAndModelNumber(String name, String modelNumber);
}
