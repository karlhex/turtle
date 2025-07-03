package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, JpaSpecificationExecutor<Invoice> {
    Optional<Invoice> findByInvoiceNo(String invoiceNo);
    
    List<Invoice> findByBuyerTaxInfoId(Long buyerTaxInfoId);
    
    List<Invoice> findBySellerTaxInfoId(Long sellerTaxInfoId);
    
    List<Invoice> findByBatchNo(String batchNo);
    
    boolean existsByInvoiceNo(String invoiceNo);
}
