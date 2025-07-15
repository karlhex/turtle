package com.fwai.turtle.modules.finance.service.impl;

import com.fwai.turtle.modules.finance.dto.InvoiceDTO;
import com.fwai.turtle.modules.finance.entity.Invoice;
import com.fwai.turtle.modules.finance.mapper.InvoiceMapper;
import com.fwai.turtle.modules.finance.repository.InvoiceRepository;
import com.fwai.turtle.modules.finance.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public InvoiceDTO findById(Long id) {
        return invoiceMapper.toDTO(invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id)));
    }

    @Override
    public InvoiceDTO findByInvoiceNo(String invoiceNo) {
        return invoiceMapper.toDTO(invoiceRepository.findByInvoiceNo(invoiceNo)
                .orElseThrow(() -> new RuntimeException("Invoice not found with invoice number: " + invoiceNo)));
    }

    @Override
    public List<InvoiceDTO> findByBuyerTaxInfoId(Long buyerTaxInfoId) {
        return invoiceMapper.toDTOs(invoiceRepository.findByBuyerTaxInfoId(buyerTaxInfoId));
    }

    @Override
    public List<InvoiceDTO> findBySellerTaxInfoId(Long sellerTaxInfoId) {
        return invoiceMapper.toDTOs(invoiceRepository.findBySellerTaxInfoId(sellerTaxInfoId));
    }

    @Override
    public List<InvoiceDTO> findByBatchNo(String batchNo) {
        return invoiceMapper.toDTOs(invoiceRepository.findByBatchNo(batchNo));
    }

    @Override
    @Transactional
    public InvoiceDTO create(InvoiceDTO invoiceDTO) {
        if (invoiceRepository.existsByInvoiceNo(invoiceDTO.getInvoiceNo())) {
            throw new RuntimeException("Invoice already exists with invoice number: " + invoiceDTO.getInvoiceNo());
        }
        
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        return invoiceMapper.toDTO(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceDTO update(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        if (!invoice.getInvoiceNo().equals(invoiceDTO.getInvoiceNo()) &&
            invoiceRepository.existsByInvoiceNo(invoiceDTO.getInvoiceNo())) {
            throw new RuntimeException("Invoice already exists with invoice number: " + invoiceDTO.getInvoiceNo());
        }
        
        invoiceMapper.updateEntityFromDTO(invoiceDTO, invoice);
        invoice.setUpdatedAt(LocalDateTime.now());
        return invoiceMapper.toDTO(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        if (invoice.getCancelled()) {
            throw new RuntimeException("Cannot delete a cancelled invoice");
        }
        
        invoiceRepository.deleteById(id);
    }

    @Override
    @Transactional
    public InvoiceDTO cancel(Long id, String reason) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));
        
        if (invoice.getCancelled()) {
            throw new RuntimeException("Invoice is already cancelled");
        }
        
        invoice.setCancelled(true);
        invoice.setCancelDate(LocalDateTime.now());
        invoice.setCancelReason(reason);
        invoice.setUpdatedAt(LocalDateTime.now());
        
        return invoiceMapper.toDTO(invoiceRepository.save(invoice));
    }
}
