package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.ContractItemDTO;
import com.fwai.turtle.service.interfaces.ContractItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contract-items")
@RequiredArgsConstructor
public class ContractItemController {
    private final ContractItemService contractItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ContractItemDTO> create(@Valid @RequestBody ContractItemDTO contractItemDTO) {
        return ApiResponse.ok(contractItemService.create(contractItemDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<ContractItemDTO> update(@PathVariable Long id, @Valid @RequestBody ContractItemDTO contractItemDTO) {
        return ApiResponse.ok(contractItemService.update(id, contractItemDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> delete(@PathVariable Long id) {
        contractItemService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<ContractItemDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(contractItemService.getById(id));
    }

    @GetMapping("/contract/{contractId}")
    public ApiResponse<List<ContractItemDTO>> getByContractId(@PathVariable Long contractId) {
        return ApiResponse.ok(contractItemService.getByContractId(contractId));
    }

    @GetMapping("/search")
    public ApiResponse<Page<ContractItemDTO>> search(
            @RequestParam Long contractId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String modelNumber,
            Pageable pageable
    ) {
        return ApiResponse.ok(
                contractItemService.search(contractId, productId, modelNumber, pageable)
        );
    }

    @DeleteMapping("/contract/{contractId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteByContractId(@PathVariable Long contractId) {
        contractItemService.deleteByContractId(contractId);
        return ApiResponse.ok(null);
    }
}
