package com.fwai.turtle.controller;

import com.fwai.turtle.dto.CompanyDTO;
import com.fwai.turtle.service.interfaces.CompanyService;
import com.fwai.turtle.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
public class CompanyController {
    private final CompanyService companyService;

    /**
     * Get paginated list of companies
     * 获取公司列表（分页）
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CompanyDTO>>> getCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean active) {
        Page<CompanyDTO> companyPage = companyService.getCompanies(PageRequest.of(page, size), search, active);
        return ResponseEntity.ok(ApiResponse.ok(companyPage));
    }

    /**
     * Search companies by name
     * 按名称搜索公司
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CompanyDTO>>> searchCompanies(
            @RequestParam String query) {
        List<CompanyDTO> companies = companyService.searchCompanies(query);
        return ResponseEntity.ok(ApiResponse.ok(companies));
    }

    /**
     * Get company by ID
     * 根据ID获取公司信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> getCompanyById(@PathVariable Long id) {
        CompanyDTO company = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.ok(company));
    }

    /**
     * Create new company
     * 创建新公司
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CompanyDTO>> createCompany(@RequestBody CompanyDTO companyDTO) {
        CompanyDTO createdCompany = companyService.createCompany(companyDTO);
        return ResponseEntity.ok(ApiResponse.ok(createdCompany));
    }

    /**
     * Update existing company
     * 更新公司信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CompanyDTO>> updateCompany(
            @PathVariable Long id,
            @RequestBody CompanyDTO companyDTO) {
        CompanyDTO updatedCompany = companyService.updateCompany(id, companyDTO);
        return ResponseEntity.ok(ApiResponse.ok(updatedCompany));
    }

    /**
     * Delete company
     * 删除公司
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    /**
     * Toggle company active status
     * 切换公司启用状态
     */
    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<CompanyDTO>> toggleStatus(@PathVariable Long id) {
        CompanyDTO updatedCompany = companyService.toggleStatus(id);
        return ResponseEntity.ok(ApiResponse.ok(updatedCompany));
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
