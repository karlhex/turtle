package com.fwai.turtle.modules.finance.repository;

import com.fwai.turtle.modules.finance.entity.TaxInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxInfoRepository extends JpaRepository<TaxInfo, Long> {
    
    /**
     * 根据启用状态查询税务信息
     */
    Page<TaxInfo> findByActive(Boolean active, Pageable pageable);

    /**
     * 根据银行账号查询税务信息
     */
    Optional<TaxInfo> findByBankAccount(String bankAccount);

    /**
     * 根据税号查询税务信息
     */
    boolean existsByTaxNo(String taxNo);

    /**
     * 根据银行账号查询税务信息
     */
    boolean existsByBankAccount(String bankAccount);

    /**
     * 获取所有启用的税务信息
     */
    List<TaxInfo> findAllByActive(Boolean active);

    /**
     * 搜索税务信息
     */
    @Query("SELECT t FROM TaxInfo t WHERE " +
           "LOWER(t.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.taxNo) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(t.bankAccount) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<TaxInfo> search(@Param("query") String query, Pageable pageable);
}
