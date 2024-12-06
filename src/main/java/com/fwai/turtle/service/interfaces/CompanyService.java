package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.CompanyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Company Service Interface
 * 公司服务接口
 */
public interface CompanyService {
    /**
     * Get paginated list of companies with optional filtering
     * 获取公司列表（分页），支持搜索和状态过滤
     */
    Page<CompanyDTO> getCompanies(Pageable pageable, String search, Boolean active);

    /**
     * Search companies by name
     * 按名称搜索公司
     */
    List<CompanyDTO> searchCompanies(String query);

    /**
     * Get company by ID
     * 根据ID获取公司信息
     */
    CompanyDTO getCompanyById(Long id);

    /**
     * Create new company
     * 创建新公司
     */
    CompanyDTO createCompany(CompanyDTO companyDTO);

    /**
     * Update existing company
     * 更新公司信息
     */
    CompanyDTO updateCompany(Long id, CompanyDTO companyDTO);

    /**
     * Delete company
     * 删除公司
     */
    void deleteCompany(Long id);

    /**
     * Toggle company active status
     * 切换公司启用状态
     */
    CompanyDTO toggleStatus(Long id);

    /**
     * Get all active companies
     * 获取所有启用的公司
     */
    List<CompanyDTO> getAllActive();
}
