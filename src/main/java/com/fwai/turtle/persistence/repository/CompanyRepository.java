package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Company Repository
 * 公司信息数据访问接口
 */
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company> {
    
    /**
     * Find companies by full name or short name
     * 根据公司全称或简称查找公司
     */
    List<Company> findByFullNameContainingIgnoreCaseOrShortNameContainingIgnoreCase(
            String fullName, String shortName);

    /**
     * Search companies by various fields
     * 多字段综合搜索公司
     */
    @Query("SELECT c FROM Company c WHERE " +
           "LOWER(c.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.shortName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.address) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.phone) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Company> searchCompanies(@Param("query") String query);

    /**
     * Check if company exists by full name
     * 检查指定全称的公司是否存在
     */
    boolean existsByFullNameIgnoreCase(String fullName);

    /**
     * Find companies by active status
     * 根据启用状态查找公司
     */
    List<Company> findByActive(Boolean active);

    /**
     * Find all active companies
     * 获取所有启用状态的公司
     */
    List<Company> findByActiveTrue();

    /**
     * Find the primary company
     * 获取主公司
     */
    Optional<Company> findByIsPrimaryTrue();

    /**
     * Reset all companies to non-primary
     * 重置所有公司为非主公司
     */
    @Modifying
    @Query("UPDATE Company c SET c.isPrimary = false")
    void resetAllPrimaryFlags();

    /**
     * Set a specific company as primary
     * 设置指定公司为主公司
     */
    @Modifying
    @Query("UPDATE Company c SET c.isPrimary = true WHERE c.id = :companyId")
    void setPrimaryCompany(@Param("companyId") Long companyId);
}
