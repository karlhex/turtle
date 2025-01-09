package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends 
    JpaRepository<Inventory, Long>, 
    JpaSpecificationExecutor<Inventory> {
    
    // Add custom query methods here if needed
}
