package com.fwai.turtle.modules.customer.service;

import com.fwai.turtle.modules.customer.dto.ContactDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Contact Service Interface
 * 联系人服务接口
 */
public interface ContactService {
    
    /**
     * Get paginated contacts
     * 获取分页的联系人列表
     */
    Page<ContactDTO> getContacts(Pageable pageable);
    
    /**
     * Search contacts
     * 搜索联系人
     */
    Page<ContactDTO> searchContacts(String query, Pageable pageable);
    
    /**
     * Get contact by ID
     * 根据ID获取联系人
     */
    ContactDTO getContactById(Long id);
    
    /**
     * Create new contact
     * 创建新联系人
     */
    ContactDTO createContact(ContactDTO contactDTO);
    
    /**
     * Update contact
     * 更新联系人信息
     */
    ContactDTO updateContact(Long id, ContactDTO contactDTO);
    
    /**
     * Delete contact
     * 删除联系人
     */
    void deleteContact(Long id);
    
    /**
     * Get contacts by company ID
     * 根据公司ID获取联系人列表
     */
    List<ContactDTO> getContactsByCompanyId(Long companyId);
    
    /**
     * Get all active contacts
     * 获取所有活跃的联系人
     */
    List<ContactDTO> getAllActiveContacts();
}
