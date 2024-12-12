package com.fwai.turtle.controller;

import com.fwai.turtle.dto.BankAccountDTO;
import com.fwai.turtle.service.interfaces.BankAccountService;
import com.fwai.turtle.common.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Bank Account Controller
 * 银行账户控制器
 */
@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    /**
     * Get bank accounts with pagination
     * 获取银行账户列表（分页）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BankAccountDTO>>> getBankAccounts(
            Pageable pageable,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) Long companyId) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankAccountService.getBankAccounts(pageable, active, companyId)));
    }

    /**
     * Get bank account by ID
     * 根据ID获取银行账户
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDTO>> getBankAccount(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(
            bankAccountService.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Bank account not found"))
        ));
    }

    /**
     * Search bank accounts
     * 搜索银行账户
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BankAccountDTO>>> searchBankAccounts(
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankAccountService.search(query, pageable)));
    }

    /**
     * Create bank account
     * 创建银行账户
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BankAccountDTO>> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankAccountService.create(bankAccountDTO)));
    }

    /**
     * Update bank account
     * 更新银行账户
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDTO>> updateBankAccount(
            @PathVariable Long id,
            @RequestBody BankAccountDTO bankAccountDTO) {
        return ResponseEntity.ok(ApiResponse.ok(
                bankAccountService.update(id, bankAccountDTO)));
    }

    /**
     * Delete bank account
     * 删除银行账户
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * Toggle bank account status
     * 切换银行账户状态（启用/禁用）
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<BankAccountDTO>> toggleStatus(@PathVariable Long id) {
        BankAccountDTO bankAccount = bankAccountService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Bank account not found"));
        return ResponseEntity.ok(ApiResponse.ok(
                bankAccountService.updateActiveStatus(id, !bankAccount.getActive())));
    }
}
