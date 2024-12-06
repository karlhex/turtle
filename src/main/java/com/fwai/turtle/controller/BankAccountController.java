package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.BankAccountDTO;
import com.fwai.turtle.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    /**
     * 获取银行账户列表（分页）
     *
     * @param pageable 分页参数
     * @param active 是否启用
     * @return 分页银行账户列表
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BankAccountDTO>>> getBankAccounts(
            Pageable pageable,
            @RequestParam(required = false) Boolean active) {
        Page<BankAccountDTO> page = bankAccountService.getBankAccounts(pageable, active);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    /**
     * 搜索银行账户
     *
     * @param query 搜索关键词
     * @param pageable 分页参数
     * @return 搜索结果
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<BankAccountDTO>>> searchBankAccounts(
            @RequestParam String query,
            Pageable pageable) {
        Page<BankAccountDTO> page = bankAccountService.searchBankAccounts(query, pageable);
        return ResponseEntity.ok(ApiResponse.ok(page));
    }

    /**
     * 根据ID获取银行账户
     *
     * @param id 银行账户ID
     * @return 银行账户
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDTO>> getBankAccount(@PathVariable Long id) {
        BankAccountDTO bankAccount = bankAccountService.getBankAccount(id);
        return ResponseEntity.ok(ApiResponse.ok(bankAccount));
    }

    /**
     * 创建银行账户
     *
     * @param bankAccountDTO 银行账户
     * @return 创建的银行账户
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BankAccountDTO>> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        BankAccountDTO created = bankAccountService.createBankAccount(bankAccountDTO);
        return ResponseEntity.ok(ApiResponse.ok(created));
    }

    /**
     * 更新银行账户
     *
     * @param bankAccountDTO 银行账户
     * @return 更新后的银行账户
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BankAccountDTO>> updateBankAccount(
            @PathVariable Long id,
            @RequestBody BankAccountDTO bankAccountDTO) {
        bankAccountDTO.setId(id);
        BankAccountDTO updated = bankAccountService.updateBankAccount(bankAccountDTO);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    /**
     * 删除银行账户
     *
     * @param id 银行账户ID
     * @return void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBankAccount(@PathVariable Long id) {
        bankAccountService.deleteBankAccount(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * 切换银行账户状态（启用/禁用）
     *
     * @param id 银行账户ID
     * @return 更新后的银行账户
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<BankAccountDTO>> toggleStatus(@PathVariable Long id) {
        BankAccountDTO updated = bankAccountService.toggleStatus(id);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}
