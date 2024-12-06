package com.fwai.turtle.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.persistence.entity.Currency;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    Optional<Currency> findByCode(String code);
    
    boolean existsByCode(String code);
    
    Optional<Currency> findByIsBaseCurrency(Boolean isBaseCurrency);
    
    Page<Currency> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(
            String code, String name, Pageable pageable);
            
    Page<Currency> findByActive(Boolean active, Pageable pageable);
    
    @Modifying
    @Query("UPDATE Currency c SET c.isBaseCurrency = false")
    void clearBaseCurrency();
}
