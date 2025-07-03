package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contact Repository
 * 联系人信息数据访问接口
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    /**
     * Find contacts by company ID
     * 根据公司ID查找联系人
     */
    List<Contact> findByCompanyId(Long companyId);
    
    /**
     * Find active contacts by company ID
     * 根据公司ID查找活跃的联系人
     */
    List<Contact> findByCompanyIdAndActiveTrue(Long companyId);
    
    /**
     * Search contacts by keyword
     * 根据关键字搜索联系人
     */
    @Query("SELECT c FROM Contact c WHERE " +
           "LOWER(c.firstName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.department) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(c.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Contact> search(@Param("query") String query, Pageable pageable);
    
    /**
     * Find all active contacts
     * 查找所有活跃的联系人
     */
    Page<Contact> findByActiveTrue(Pageable pageable);
}
