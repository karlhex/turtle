package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.TaxInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxInfoRepository extends JpaRepository<TaxInfo, Long> {
    
    /**
     * 根据启用状态查询税务信息
     */
    Page<TaxInfo> findByActive(Boolean active, Pageable pageable);

    /**
     * 根据税号查询税务信息
     */
    boolean existsByTaxNo(String taxNo);

    /**
     * 根据银行账号查询税务信息
     */
    boolean existsByBankAccount(String bankAccount);

    /**
     * 搜索税务信息
     */
    @Query("SELECT t FROM TaxInfo t WHERE " +
           "LOWER(t.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.taxNo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.bankAccount) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<TaxInfo> search(@Param("query") String query, Pageable pageable);
}
