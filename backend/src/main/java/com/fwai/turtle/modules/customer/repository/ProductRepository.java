package com.fwai.turtle.modules.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.modules.customer.entity.Product;
import com.fwai.turtle.base.types.ProductType;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.modelNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.manufacturer.fullName) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);
            
    Page<Product> findByType(ProductType type, Pageable pageable);
    
    Page<Product> findByActive(Boolean active, Pageable pageable);
    
    boolean existsByNameAndModelNumber(String name, String modelNumber);
}
