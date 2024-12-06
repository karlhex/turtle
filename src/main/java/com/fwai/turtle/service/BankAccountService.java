package com.fwai.turtle.service;

import com.fwai.turtle.dto.BankAccountDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.exception.DuplicateRecordException;
import com.fwai.turtle.persistence.entity.BankAccount;
import com.fwai.turtle.persistence.mapper.BankAccountMapper;
import com.fwai.turtle.persistence.repository.BankAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    /**
     * 获取银行账户列表（分页）
     *
     * @param pageable 分页参数
     * @param active 是否启用
     * @return 分页银行账户列表
     */
    public Page<BankAccountDTO> getBankAccounts(Pageable pageable, Boolean active) {
        Page<BankAccount> page;
        if (active != null) {
            page = bankAccountRepository.findByActive(active, pageable);
        } else {
            page = bankAccountRepository.findAll(pageable);
        }
        return page.map(bankAccountMapper::toDTO);
    }

    /**
     * 根据ID获取银行账户
     *
     * @param id 银行账户ID
     * @return 银行账户
     */
    public BankAccountDTO getBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount not found with id: " + id));
        return bankAccountMapper.toDTO(bankAccount);
    }

    /**
     * 创建银行账户
     *
     * @param bankAccountDTO 银行账户
     * @return 创建的银行账户
     */
    @Transactional
    public BankAccountDTO createBankAccount(BankAccountDTO bankAccountDTO) {
        // 验证账号是否已存在
        if (bankAccountRepository.existsByAccountNo(bankAccountDTO.getAccountNo())) {
            throw new DuplicateRecordException("Bank account number already exists: " + bankAccountDTO.getAccountNo());
        }

        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount.setActive(true);
        bankAccount.setCreatedAt(LocalDateTime.now());
        bankAccount.setUpdatedAt(LocalDateTime.now());
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }

    /**
     * 更新银行账户
     *
     * @param bankAccountDTO 银行账户
     * @return 更新后的银行账户
     */
    @Transactional
    public BankAccountDTO updateBankAccount(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount not found with id: " + bankAccountDTO.getId()));

        // 验证账号是否被其他记录使用
        if (!bankAccount.getAccountNo().equals(bankAccountDTO.getAccountNo()) &&
            bankAccountRepository.existsByAccountNo(bankAccountDTO.getAccountNo())) {
            throw new DuplicateRecordException("Bank account number already exists: " + bankAccountDTO.getAccountNo());
        }

        bankAccountMapper.updateEntityFromDTO(bankAccountDTO, bankAccount);
        bankAccount.setUpdatedAt(LocalDateTime.now());
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }

    /**
     * 删除银行账户
     *
     * @param id 银行账户ID
     */
    @Transactional
    public void deleteBankAccount(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount not found with id: " + id));
        bankAccountRepository.delete(bankAccount);
    }

    /**
     * 切换银行账户状态
     *
     * @param id 银行账户ID
     * @return 更新后的银行账户
     */
    @Transactional
    public BankAccountDTO toggleStatus(Long id) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BankAccount not found with id: " + id));
        
        bankAccount.setActive(!bankAccount.getActive());
        bankAccount.setUpdatedAt(LocalDateTime.now());
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }

    /**
     * 搜索银行账户
     *
     * @param query 搜索关键词
     * @param pageable 分页参数
     * @return 搜索结果
     */
    public Page<BankAccountDTO> searchBankAccounts(String query, Pageable pageable) {
        return bankAccountRepository.search(query, pageable)
                .map(bankAccountMapper::toDTO);
    }
}
