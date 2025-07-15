package com.fwai.turtle.modules.project.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.project.dto.ContractDownPaymentDTO;
import com.fwai.turtle.modules.project.service.ContractDownPaymentService;
import com.fwai.turtle.base.types.DebitCreditType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/contract-payments")
@RequiredArgsConstructor
public class ContractDownPaymentController {
    private final ContractDownPaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContractDownPaymentDTO> create(@Valid @RequestBody ContractDownPaymentDTO paymentDTO) {
        return ApiResponse.ok(paymentService.create(paymentDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<ContractDownPaymentDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ContractDownPaymentDTO paymentDTO
    ) {
        return ApiResponse.ok(paymentService.update(id, paymentDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<ContractDownPaymentDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(paymentService.getById(id));
    }

    @GetMapping("/contract/{contractId}")
    public ApiResponse<List<ContractDownPaymentDTO>> getByContractId(@PathVariable Long contractId) {
        return ApiResponse.ok(paymentService.getByContractId(contractId));
    }

    @GetMapping("/search")
    public ApiResponse<Page<ContractDownPaymentDTO>> search(
            @RequestParam Long contractId,
            @RequestParam(required = false) DebitCreditType debitCreditType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) Boolean paymentStatus,
            Pageable pageable
    ) {
        return ApiResponse.ok(
                paymentService.search(contractId, debitCreditType, startDate, endDate, paymentStatus, pageable)
        );
    }

    @GetMapping("/overdue")
    public ApiResponse<List<ContractDownPaymentDTO>> getOverduePayments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(paymentService.getOverduePayments(date));
    }

    @PatchMapping("/{id}/confirm")
    public ApiResponse<ContractDownPaymentDTO> confirmPayment(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate actualDate
    ) {
        return ApiResponse.ok(paymentService.confirmPayment(id, actualDate));
    }

    @DeleteMapping("/contract/{contractId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteByContractId(@PathVariable Long contractId) {
        paymentService.deleteByContractId(contractId);
        return ApiResponse.ok(null);
    }
}
