package com.fwai.turtle.controller;

import com.fwai.turtle.dto.ContactDTO;
import com.fwai.turtle.service.interfaces.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contact Controller
 * 联系人控制器
 */
@RestController
@RequestMapping("/api/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    /**
     * Get paginated contacts
     * 获取分页的联系人列表
     */
    @GetMapping
    public ResponseEntity<Page<ContactDTO>> getContacts(Pageable pageable) {
        return ResponseEntity.ok(contactService.getContacts(pageable));
    }

    /**
     * Search contacts
     * 搜索联系人
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ContactDTO>> searchContacts(
            @RequestParam String query,
            Pageable pageable) {
        return ResponseEntity.ok(contactService.searchContacts(query, pageable));
    }

    /**
     * Get contact by ID
     * 根据ID获取联系人
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(@PathVariable Long id) {
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    /**
     * Create new contact
     * 创建新联系人
     */
    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.createContact(contactDTO));
    }

    /**
     * Update contact
     * 更新联系人信息
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(
            @PathVariable Long id,
            @RequestBody ContactDTO contactDTO) {
        return ResponseEntity.ok(contactService.updateContact(id, contactDTO));
    }

    /**
     * Delete contact
     * 删除联系人
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Get contacts by company ID
     * 根据公司ID获取联系人列表
     */
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<ContactDTO>> getContactsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(contactService.getContactsByCompanyId(companyId));
    }

    /**
     * Get all active contacts
     * 获取所有活跃的联系人
     */
    @GetMapping("/active")
    public ResponseEntity<List<ContactDTO>> getAllActiveContacts() {
        return ResponseEntity.ok(contactService.getAllActiveContacts());
    }
}
