package com.fwai.turtle.modules.finance.controller;

import com.fwai.turtle.modules.finance.dto.InvoiceDTO;
import com.fwai.turtle.modules.finance.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @GetMapping("/no/{invoiceNo}")
    public ResponseEntity<InvoiceDTO> getByInvoiceNo(@PathVariable String invoiceNo) {
        return ResponseEntity.ok(invoiceService.findByInvoiceNo(invoiceNo));
    }

    @GetMapping("/buyer/{buyerTaxInfoId}")
    public ResponseEntity<List<InvoiceDTO>> getByBuyerTaxInfoId(@PathVariable Long buyerTaxInfoId) {
        return ResponseEntity.ok(invoiceService.findByBuyerTaxInfoId(buyerTaxInfoId));
    }

    @GetMapping("/seller/{sellerTaxInfoId}")
    public ResponseEntity<List<InvoiceDTO>> getBySellerTaxInfoId(@PathVariable Long sellerTaxInfoId) {
        return ResponseEntity.ok(invoiceService.findBySellerTaxInfoId(sellerTaxInfoId));
    }

    @GetMapping("/batch/{batchNo}")
    public ResponseEntity<List<InvoiceDTO>> getByBatchNo(@PathVariable String batchNo) {
        return ResponseEntity.ok(invoiceService.findByBatchNo(batchNo));
    }

    @PostMapping
    public ResponseEntity<InvoiceDTO> create(@RequestBody InvoiceDTO invoiceDTO) {
        return ResponseEntity.ok(invoiceService.create(invoiceDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> update(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
        return ResponseEntity.ok(invoiceService.update(id, invoiceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        invoiceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<InvoiceDTO> cancel(@PathVariable Long id, @RequestParam String reason) {
        return ResponseEntity.ok(invoiceService.cancel(id, reason));
    }
}
