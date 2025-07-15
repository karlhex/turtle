package com.fwai.turtle.modules.finance.service;

import com.fwai.turtle.modules.finance.dto.BankAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Bank Account Service
 * 银行账户服务接口
 */
public interface BankAccountService {
    
    /**
     * Create a new bank account
     * 创建银行账户
     */
    BankAccountDTO create(BankAccountDTO bankAccountDTO);
    
    /**
     * Update bank account
     * 更新银行账户
     */
    BankAccountDTO update(Long id, BankAccountDTO bankAccountDTO);
    
    /**
     * Get bank account by ID
     * 根据ID获取银行账户
     */
    Optional<BankAccountDTO> findById(Long id);
    
    /**
     * Get all bank accounts with pagination
     * 分页获取所有银行账户
     */
    Page<BankAccountDTO> findAll(Pageable pageable);
    
    /**
     * Get bank accounts by company ID
     * 根据公司ID获取银行账户
     */
    List<BankAccountDTO> findByCompanyId(Long companyId);
    
    /**
     * Delete bank account by ID
     * 根据ID删除银行账户
     */
    void deleteById(Long id);
    
    /**
     * Check if account number exists
     * 检查账号是否存在
     */
    boolean existsByAccountNo(String accountNo);
    
    /**
     * Activate/deactivate bank account
     * 激活/停用银行账户
     */
    BankAccountDTO updateActiveStatus(Long id, boolean active);
    
    /**
     * Get bank accounts with optional filtering
     * 获取银行账户（带可选过滤）
     */
    Page<BankAccountDTO> getBankAccounts(Pageable pageable, Boolean active, Long companyId);

    /**
     * Search bank accounts by query string
     * 搜索银行账户
     */
    Page<BankAccountDTO> search(String query, Pageable pageable);
}
