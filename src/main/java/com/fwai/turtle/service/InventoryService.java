package com.fwai.turtle.service;

import com.fwai.turtle.dto.InventoryDTO;
import com.fwai.turtle.persistence.entity.Inventory;
import com.fwai.turtle.persistence.mapper.InventoryMapper;
import com.fwai.turtle.persistence.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fwai.turtle.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;

    @Transactional
    public InventoryDTO create(InventoryDTO inventoryDTO) {
        Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return inventoryMapper.toDTO(savedInventory);
    }

    @Transactional(readOnly = true)
    public InventoryDTO findById(Long id) {
        return inventoryRepository.findById(id)
                .map(inventoryMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<InventoryDTO> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable)
                .map(inventoryMapper::toDTO);
    }

    @Transactional
    public InventoryDTO update(Long id, InventoryDTO inventoryDTO) {
        return inventoryRepository.findById(id)
                .map(existingInventory -> {
                    Inventory updatedInventory = inventoryMapper.toEntity(inventoryDTO);
                    updatedInventory.setId(existingInventory.getId());
                    return inventoryMapper.toDTO(inventoryRepository.save(updatedInventory));
                })
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
    }

    @Transactional
    public void deleteById(Long id) {
        inventoryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<InventoryDTO> findAllById(List<Long> ids) {
        return inventoryRepository.findAllById(ids)
                .stream()
                .map(inventoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
