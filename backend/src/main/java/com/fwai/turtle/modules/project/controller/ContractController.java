package com.fwai.turtle.modules.project.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.project.dto.ContractDTO;
import com.fwai.turtle.modules.project.dto.ContractItemDTO;
import com.fwai.turtle.modules.project.service.ContractService;
import com.fwai.turtle.base.types.ContractStatus;
import com.fwai.turtle.base.types.ContractType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {
    private final ContractService contractService;

    @GetMapping
    public ApiResponse<Page<ContractDTO>> getContracts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String sortBy,
        @RequestParam(required = false) String direction) {
      log.info("getUsers - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);
      
      Pageable pageable;
      if (sortBy != null && direction != null) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        pageable = PageRequest.of(page, size, sort);
      } else {
        pageable = PageRequest.of(page, size);
      }
      
      return ApiResponse.ok(contractService.findAll(pageable));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContractDTO> create(@Valid @RequestBody ContractDTO contractDTO) {
        return ApiResponse.ok(contractService.create(contractDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<ContractDTO> update(@PathVariable Long id, @Valid @RequestBody ContractDTO contractDTO) {
        return ApiResponse.ok(contractService.update(id, contractDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long id) {
        contractService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<ContractDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(contractService.getById(id));
    }

    @GetMapping("/no/{contractNo}")
    public ApiResponse<ContractDTO> getByContractNo(@PathVariable String contractNo) {
        return ApiResponse.ok(contractService.getByContractNo(contractNo));
    }

    @GetMapping("/search")
    public ApiResponse<Page<ContractDTO>> search(
            @RequestParam(required = false) String contractNo,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) ContractType type,
            @RequestParam(required = false) ContractStatus status,
            @RequestParam(required = false) String projectNo,
            Pageable pageable
    ) {
        return ApiResponse.ok(
                contractService.search(contractNo, title, company, type, status, projectNo, pageable)
        );
    }

    @GetMapping("/status/{status}")
    public ApiResponse<List<ContractDTO>> getByStatus(@PathVariable ContractStatus status) {
        return ApiResponse.ok(contractService.getByStatus(status));
    }

    @GetMapping("/expired")
    public ApiResponse<List<ContractDTO>> getExpiredContracts(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        return ApiResponse.ok(contractService.getExpiredContracts(date));
    }

    @GetMapping("/invoice/no/{invoiceNo}")
    public ApiResponse<List<ContractDTO>> getContractsByInvoiceNo(@PathVariable String invoiceNo) {
        return ApiResponse.ok(contractService.getContractsByInvoiceNo(invoiceNo));
    }

    @GetMapping("/invoice/{invoiceId}")
    public ApiResponse<ContractDTO> getContractByInvoiceId(@PathVariable Long invoiceId) {
        return ApiResponse.ok(contractService.getContractByInvoiceId(invoiceId));
    }

    @GetMapping("/date-range")
    public ApiResponse<List<ContractDTO>> getContractsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ApiResponse.ok(contractService.getContractsByDateRange(startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<ContractDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam ContractStatus status
    ) {
        return ApiResponse.ok(contractService.updateStatus(id, status));
    }

    // Contract Items endpoints
    @PostMapping("/{contractId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContractDTO> addContractItem(
            @PathVariable Long contractId,
            @Valid @RequestBody ContractItemDTO itemDTO
    ) {
        return ApiResponse.ok(contractService.addContractItem(contractId, itemDTO));
    }

    @PutMapping("/{contractId}/items/{itemId}")
    public ApiResponse<ContractDTO> updateContractItem(
            @PathVariable Long contractId,
            @PathVariable Long itemId,
            @Valid @RequestBody ContractItemDTO itemDTO
    ) {
        return ApiResponse.ok(contractService.updateContractItem(contractId, itemId, itemDTO));
    }

    @DeleteMapping("/{contractId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<ContractDTO> removeContractItem(
            @PathVariable Long contractId,
            @PathVariable Long itemId   
    ) {
        return ApiResponse.ok(contractService.removeContractItem(contractId, itemId));
    }

    @GetMapping("/{contractId}/items")
    public ApiResponse<List<ContractItemDTO>> getContractItems(@PathVariable Long contractId) {
        return ApiResponse.ok(contractService.getContractItems(contractId));
    }

    @PutMapping("/{contractId}/items")
    public ApiResponse<ContractDTO> updateContractItems(
            @PathVariable Long contractId,
            @Valid @RequestBody List<ContractItemDTO> items
    ) {
        return ApiResponse.ok(contractService.updateContractItems(contractId, items));
    }
}
