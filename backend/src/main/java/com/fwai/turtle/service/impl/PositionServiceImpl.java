package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.PositionDTO;
import com.fwai.turtle.persistence.entity.Position;
import com.fwai.turtle.persistence.mapper.PositionMapper;
import com.fwai.turtle.persistence.repository.PositionRepository;
import com.fwai.turtle.service.interfaces.PositionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    @Override
    @Transactional
    public PositionDTO create(PositionDTO positionDTO) {
        Position position = positionMapper.toEntity(positionDTO);
        position = positionRepository.save(position);
        return positionMapper.toDTO(position);
    }

    @Override
    @Transactional
    public PositionDTO update(Long id, PositionDTO positionDTO) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));
        
        // Update position properties
        position.setName(positionDTO.getName());
        position.setDescription(positionDTO.getDescription());
        position.setIsActive(positionDTO.getIsActive());
        
        position = positionRepository.save(position);
        
        return positionMapper.toDTO(position);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        positionRepository.deleteById(id);
    }

    @Override
    public PositionDTO findById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));
        return positionMapper.toDTO(position);
    }

    @Override
    public Page<PositionDTO> findAll(Pageable pageable) {
        return positionRepository.findAll(pageable).map(positionMapper::toDTO);
    }

    @Override
    public List<PositionDTO> findActive() {
        return positionRepository.findByIsActiveTrue().stream()
                .map(positionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
