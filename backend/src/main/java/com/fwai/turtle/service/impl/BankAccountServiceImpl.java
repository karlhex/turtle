package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.BankAccountDTO;
import com.fwai.turtle.persistence.entity.BankAccount;
import com.fwai.turtle.persistence.mapper.BankAccountMapper;
import com.fwai.turtle.persistence.repository.BankAccountRepository;
import com.fwai.turtle.service.interfaces.BankAccountService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Bank Account Service Implementation
 * 银行账户服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Override
    @Transactional
    public BankAccountDTO create(BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }

    @Override
    @Transactional
    public BankAccountDTO update(Long id, BankAccountDTO bankAccountDTO) {
        BankAccount existingAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found with id: " + id));
        
        bankAccountMapper.updateEntityFromDTO(bankAccountDTO, existingAccount);
        existingAccount = bankAccountRepository.save(existingAccount);
        return bankAccountMapper.toDTO(existingAccount);
    }

    @Override
    public Optional<BankAccountDTO> findById(Long id) {
        return bankAccountRepository.findById(id)
                .map(bankAccountMapper::toDTO);
    }

    @Override
    public Page<BankAccountDTO> findAll(Pageable pageable) {
        return bankAccountRepository.findAll(pageable)
                .map(bankAccountMapper::toDTO);
    }

    @Override
    public List<BankAccountDTO> findByCompanyId(Long companyId) {
        return bankAccountRepository.findByCompanyId(companyId).stream()
                .map(bankAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bankAccountRepository.deleteById(id);
    }

    @Override
    public boolean existsByAccountNo(String accountNo) {
        return bankAccountRepository.existsByAccountNo(accountNo);
    }

    @Override
    @Transactional
    public BankAccountDTO updateActiveStatus(Long id, boolean active) {
        BankAccount bankAccount = bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found with id: " + id));
        
        bankAccount.setActive(active);
        bankAccount = bankAccountRepository.save(bankAccount);
        return bankAccountMapper.toDTO(bankAccount);
    }

    @Override
    public Page<BankAccountDTO> getBankAccounts(Pageable pageable, Boolean active, Long companyId) {
        if (active != null && companyId != null) {
            return bankAccountRepository.findByActiveAndCompanyId(active, companyId, pageable)
                    .map(bankAccountMapper::toDTO);
        } else if (active != null) {
            return bankAccountRepository.findByActive(active, pageable)
                    .map(bankAccountMapper::toDTO);
        } else if (companyId != null) {
            return bankAccountRepository.findByCompanyId(companyId, pageable)
                    .map(bankAccountMapper::toDTO);
        } else {
            return findAll(pageable);
        }
    }

    @Override
    public Page<BankAccountDTO> search(String query, Pageable pageable) {
        return bankAccountRepository.findByAccountNoContainingOrBankNameContainingOrBankBranchContaining(
                query, query, query, pageable)
                .map(bankAccountMapper::toDTO);
    }
}
