package com.fwai.turtle.modules.customer.service.impl;

import com.fwai.turtle.modules.customer.dto.ContactDTO;
import com.fwai.turtle.modules.customer.entity.Contact;
import com.fwai.turtle.modules.customer.mapper.ContactMapper;
import com.fwai.turtle.modules.customer.repository.ContactRepository;
import com.fwai.turtle.modules.customer.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Contact Service Implementation
 * 联系人服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDTO> getContacts(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(contactMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactDTO> searchContacts(String query, Pageable pageable) {
        return contactRepository.search(query, pageable)
                .map(contactMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDTO getContactById(Long id) {
        return contactRepository.findById(id)
                .map(contactMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    @Override
    public ContactDTO createContact(ContactDTO contactDTO) {
        Contact contact = contactMapper.toEntity(contactDTO);
        contact = contactRepository.save(contact);
        return contactMapper.toDTO(contact);
    }

    @Override
    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Contact existingContact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        
        contactMapper.updateEntityFromDTO(contactDTO, existingContact);
        existingContact = contactRepository.save(existingContact);
        return contactMapper.toDTO(existingContact);
    }

    @Override
    public void deleteContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
        contact.setActive(false);
        contactRepository.save(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDTO> getContactsByCompanyId(Long companyId) {
        return contactRepository.findByCompanyId(companyId).stream()
                .map(contactMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactDTO> getAllActiveContacts() {
        return contactRepository.findByActiveTrue(Pageable.unpaged()).stream()
                .map(contactMapper::toDTO)
                .collect(Collectors.toList());
    }
}
