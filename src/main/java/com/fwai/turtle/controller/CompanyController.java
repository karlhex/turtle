package com.fwai.turtle.controller;

import com.fwai.turtle.dto.CompanyDTO;
import com.fwai.turtle.dto.BankAccountDTO;
import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.service.interfaces.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Company Controller
 * 公司信息管理接口
 */
@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
@Tag(name = "Company", description = "Company management APIs")
public class CompanyController {
    private final CompanyService companyService;

    /**
     * Get paginated list of companies
     * 获取公司列表（分页）
     */
    @GetMapping
    @Operation(summary = "Get paginated list of companies")
    public ResponseEntity<ApiResponse<Page<CompanyDTO>>> getCompanies(
            Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.getCompanies(pageable, search, active)));
    }

    /**
     * Search companies by name
     * 按名称搜索公司
     */
    @GetMapping("/search")
    @Operation(summary = "Search companies by name")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> searchCompanies(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.searchCompanies(query)));
    }

    /**
     * Get company by ID
     * 根据ID获取公司信息
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get company by ID")
    public ResponseEntity<ApiResponse<CompanyDTO>> getCompany(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.getCompanyById(id)));
    }

    /**
     * Create new company
     * 创建新公司
     */
    @PostMapping
    @Operation(summary = "Create new company")
    public ResponseEntity<ApiResponse<CompanyDTO>> createCompany(@Valid @RequestBody CompanyDTO companyDTO) {
        return new ResponseEntity<>(ApiResponse.ok(companyService.createCompany(companyDTO)), HttpStatus.CREATED);
    }

    /**
     * Update existing company
     * 更新公司信息
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update existing company")
    public ResponseEntity<ApiResponse<CompanyDTO>> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyDTO companyDTO) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.updateCompany(id, companyDTO)));
    }

    /**
     * Delete company
     * 删除公司
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Toggle company active status
     * 切换公司启用状态
     */
    @PutMapping("/{id}/toggle-status")
    @Operation(summary = "Toggle company active status")
    public ResponseEntity<ApiResponse<CompanyDTO>> toggleStatus(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.toggleCompanyStatus(id)));
    }

    /**
     * Get primary company
     * 获取主公司
     */
    @GetMapping("/primary")
    @Operation(summary = "Get primary company")
    public ResponseEntity<ApiResponse<CompanyDTO>> getPrimaryCompany() {
        CompanyDTO primaryCompany = companyService.getPrimaryCompany();
        return primaryCompany != null ? 
               ResponseEntity.ok(ApiResponse.ok(primaryCompany)) : 
               ResponseEntity.notFound().build();
    }

    /**
     * Set company as primary
     * 设置主公司
     */
    @PutMapping("/{id}/set-primary")
    @Operation(summary = "Set company as primary", description = "Set a company as the primary company")
    public ResponseEntity<ApiResponse<CompanyDTO>> setPrimary(@PathVariable Long id) {
        CompanyDTO company = companyService.setPrimaryCompany(id);
        return ResponseEntity.ok(ApiResponse.ok(company));
    }

    /**
     * Get company bank accounts
     * 获取公司银行账户
     */
    @GetMapping("/{id}/bank-accounts")
    @Operation(summary = "Get company bank accounts")
    public ResponseEntity<ApiResponse<List<BankAccountDTO>>> getCompanyBankAccounts(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.getCompanyBankAccounts(id)));
    }

    /**
     * Add bank account to company
     * 添加银行账户到公司
     */
    @PostMapping("/{id}/bank-accounts")
    @Operation(summary = "Add bank account to company")
    public ResponseEntity<ApiResponse<CompanyDTO>> addBankAccount(
            @PathVariable Long id,
            @Valid @RequestBody BankAccountDTO bankAccountDTO) {
        return ResponseEntity.ok(ApiResponse.ok(companyService.addBankAccount(id, bankAccountDTO)));
    }

    /**
     * Remove bank account from company
     * 从公司中删除银行账户
     */
    @DeleteMapping("/{companyId}/bank-accounts/{bankAccountId}")
    @Operation(summary = "Remove bank account from company")
    public ResponseEntity<ApiResponse<Void>> removeBankAccount(
            @PathVariable Long companyId,
            @PathVariable Long bankAccountId) {
        companyService.removeBankAccount(companyId, bankAccountId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all active companies
     * 获取所有启用状态的公司
     */
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> getAllActive() {
        List<CompanyDTO> activeCompanies = companyService.getAllActive();
        return ResponseEntity.ok(ApiResponse.ok(activeCompanies));
    }
}
