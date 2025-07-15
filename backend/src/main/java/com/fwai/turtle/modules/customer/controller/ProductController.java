package com.fwai.turtle.modules.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.customer.dto.ProductDTO;
import com.fwai.turtle.modules.customer.service.ProductService;
import com.fwai.turtle.base.types.ProductType;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ApiResponse<ProductDTO> create(@Valid @RequestBody ProductDTO productDTO) {
        return ApiResponse.ok(productService.create(productDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        return ApiResponse.ok(productService.update(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(productService.getById(id));
    }

    @GetMapping
    public ApiResponse<Page<ProductDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ApiResponse.ok(productService.getAll(pageable));
    }

    @GetMapping("/active")
    public ApiResponse<Page<ProductDTO>> getAllActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ApiResponse.ok(productService.getAllActive(pageable));
    }

    @GetMapping("/search")
    public ApiResponse<Page<ProductDTO>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ApiResponse.ok(productService.search(query, pageable));
    }

    @GetMapping("/type/{type}")
    public ApiResponse<Page<ProductDTO>> getByType(
            @PathVariable ProductType type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ApiResponse.ok(productService.getByType(type, pageable));
    }

    @PutMapping("/{id}/toggle-status")
    public ApiResponse<Void> toggleStatus(@PathVariable Long id) {
        productService.toggleStatus(id);
        return ApiResponse.ok(null);
    }
}
