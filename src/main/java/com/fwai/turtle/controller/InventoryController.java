package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.InventoryDTO;
import com.fwai.turtle.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventories")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;

    @PostMapping
    public ApiResponse<InventoryDTO> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO createdInventory = inventoryService.create(inventoryDTO);
        return ApiResponse.ok(createdInventory);
    }

    @GetMapping("/{id}")
    public ApiResponse<InventoryDTO> getInventoryById(@PathVariable Long id) {
        InventoryDTO inventory = inventoryService.findById(id);
        return ApiResponse.ok(inventory);
    }

    @GetMapping
    public ApiResponse<Page<InventoryDTO>> getAllInventories(Pageable pageable) {
        Page<InventoryDTO> inventories = inventoryService.findAll(pageable);
        return ApiResponse.ok(inventories);
    }

    @PutMapping("/{id}/outbound")
    public ApiResponse<InventoryDTO> outboundInventory(
            @PathVariable Long id, 
            @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO updatedInventory = inventoryService.outBoundItem(id, inventoryDTO);
        return ApiResponse.ok(updatedInventory);
    }

    @PutMapping("/{id}/borrow")
    public ApiResponse<InventoryDTO> borrowInventory(
            @PathVariable Long id, 
            @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO updatedInventory = inventoryService.borrowItem(id, inventoryDTO);
        return ApiResponse.ok(updatedInventory);
    }

    @PutMapping("/{id}/return")
    public ApiResponse<InventoryDTO> returnInventory(
            @PathVariable Long id, 
            @RequestBody InventoryDTO inventoryDTO) {
        InventoryDTO updatedInventory = inventoryService.returnItem(id, inventoryDTO);
        return ApiResponse.ok(updatedInventory);
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteInventory(@PathVariable Long id) {
        inventoryService.deleteById(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/bulk")
    public ApiResponse<List<InventoryDTO>> getInventoriesByIds(@RequestBody List<Long> ids) {
        List<InventoryDTO> inventories = inventoryService.findAllById(ids);
        return ApiResponse.ok(inventories);
    }
}
