package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    /**
     * Check if account number exists
     * 检查账号是否存在
     */
    boolean existsByAccountNo(String accountNo);

    /**
     * Find by company ID
     * 根据公司ID查询
     */
    List<BankAccount> findByCompanyId(Long companyId);

    /**
     * Find by company ID and active status
     * 根据公司ID和启用状态查询
     */
    Page<BankAccount> findByCompanyIdAndActive(Long companyId, Boolean active, Pageable pageable);

    /**
     * Find by active status
     * 根据启用状态查询
     */
    Page<BankAccount> findByActive(Boolean active, Pageable pageable);

    /**
     * Find by company ID with pagination
     * 根据公司ID分页查询
     */
    Page<BankAccount> findByCompanyId(Long companyId, Pageable pageable);

    /**
     * Find by active status and company ID with pagination
     * 根据启用状态和公司ID分页查询
     */
    Page<BankAccount> findByActiveAndCompanyId(Boolean active, Long companyId, Pageable pageable);

    /**
     * Search bank accounts by account number, bank name, or branch name
     * 根据账号、银行名称或支行名称搜索银行账户
     */
    Page<BankAccount> findByAccountNoContainingOrBankNameContainingOrBankBranchContaining(
            String accountNo, String bankName, String bankBranch, Pageable pageable);
}
