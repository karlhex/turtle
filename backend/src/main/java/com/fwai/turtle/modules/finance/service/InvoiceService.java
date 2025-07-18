package com.fwai.turtle.modules.finance.service;

import com.fwai.turtle.modules.finance.dto.InvoiceDTO;
import java.util.List;

public interface InvoiceService {
    InvoiceDTO findById(Long id);
    
    InvoiceDTO findByInvoiceNo(String invoiceNo);
    
    List<InvoiceDTO> findByBuyerTaxInfoId(Long buyerTaxInfoId);
    
    List<InvoiceDTO> findBySellerTaxInfoId(Long sellerTaxInfoId);
    
    List<InvoiceDTO> findByBatchNo(String batchNo);
    
    InvoiceDTO create(InvoiceDTO invoiceDTO);
    
    InvoiceDTO update(Long id, InvoiceDTO invoiceDTO);
    
    void delete(Long id);
    
    InvoiceDTO cancel(Long id, String reason);
}
