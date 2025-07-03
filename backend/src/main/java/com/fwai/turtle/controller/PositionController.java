package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.PositionDTO;
import com.fwai.turtle.service.interfaces.PositionService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/positions")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService positionService;

    @PostMapping
    public ApiResponse<PositionDTO> create(@RequestBody PositionDTO positionDTO) {
        return ApiResponse.ok(positionService.create(positionDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<PositionDTO> update(@PathVariable Long id, @RequestBody PositionDTO positionDTO) {
        return ApiResponse.ok(positionService.update(id, positionDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        positionService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    public ApiResponse<PositionDTO> findById(@PathVariable Long id) {
        return ApiResponse.ok(positionService.findById(id));
    }

    @GetMapping
    public ApiResponse<Page<PositionDTO>> findAll(Pageable pageable) {
        return ApiResponse.ok(positionService.findAll(pageable));
    }

    @GetMapping("/active")
    public ApiResponse<List<PositionDTO>> findActive() {
        return ApiResponse.ok(positionService.findActive());
    }
}
