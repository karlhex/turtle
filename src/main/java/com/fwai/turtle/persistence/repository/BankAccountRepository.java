package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    
    /**
     * Find bank accounts by active status
     */
    Page<BankAccount> findByActive(Boolean active, Pageable pageable);

    /**
     * Check if account number exists
     */
    boolean existsByAccountNo(String accountNo);

    /**
     * Search bank accounts by account name or account number
     */
    @Query("SELECT b FROM BankAccount b WHERE " +
           "LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(b.accountNo) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<BankAccount> search(@Param("query") String query, Pageable pageable);
}
