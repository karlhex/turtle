package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.PositionDTO;
import com.fwai.turtle.persistence.entity.Position;
import com.fwai.turtle.persistence.mapper.PositionMapper;
import com.fwai.turtle.persistence.repository.DepartmentRepository;
import com.fwai.turtle.persistence.repository.PositionRepository;
import com.fwai.turtle.service.interfaces.PositionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fwai.turtle.persistence.entity.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public PositionDTO create(PositionDTO positionDTO) {
        Position position = PositionMapper.INSTANCE.toEntity(positionDTO);
        Department department = departmentRepository.findById(positionDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + positionDTO.getDepartmentId()));
        position.setDepartment(department);
        position = positionRepository.save(position);
        return PositionMapper.INSTANCE.toDTO(position);
    }

    @Override
    @Transactional
    public PositionDTO update(Long id, PositionDTO positionDTO) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found with id: " + id));
        
        Position updatedPosition = PositionMapper.INSTANCE.toEntity(positionDTO);
        Department department = departmentRepository.findById(positionDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + positionDTO.getDepartmentId()));
        updatedPosition.setDepartment(department);
        updatedPosition.setId(id);
        updatedPosition = positionRepository.save(updatedPosition);
        
        return PositionMapper.INSTANCE.toDTO(updatedPosition);
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
        return PositionMapper.INSTANCE.toDTO(position);
    }

    @Override
    public Page<PositionDTO> findAll(Pageable pageable) {
        return positionRepository.findAll(pageable).map(PositionMapper.INSTANCE::toDTO);
    }

    @Override
    public List<PositionDTO> findByDepartmentId(Long departmentId) {
        return positionRepository.findByDepartmentId(departmentId).stream()
                .map(PositionMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PositionDTO> findActive() {
        return positionRepository.findByIsActiveTrue().stream()
                .map(PositionMapper.INSTANCE::toDTO)
                .collect(Collectors.toList());
    }
}
