package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.modules.organization.dto.DepartmentDTO;
import com.fwai.turtle.modules.organization.entity.Department;
import com.fwai.turtle.modules.organization.mapper.DepartmentMapper;
import com.fwai.turtle.modules.organization.repository.DepartmentRepository;
import com.fwai.turtle.modules.organization.service.DepartmentService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public Page<DepartmentDTO> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable)
                .map(departmentMapper::toDTO);
    }

    @Override
    public DepartmentDTO findById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    }

    @Override
    @Transactional
    public DepartmentDTO create(DepartmentDTO departmentDTO) {
        Department department = departmentMapper.toEntity(departmentDTO);
        department = departmentRepository.save(department);
        return departmentMapper.toDTO(department);
    }

    @Override
    @Transactional
    public DepartmentDTO update(Long id, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        
        departmentMapper.updateEntityFromDTO(departmentDTO, department);
        department = departmentRepository.save(department);
        return departmentMapper.toDTO(department);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
        departmentRepository.delete(department);
    }

    @Override
    public Page<DepartmentDTO> search(String query, Pageable pageable) {
        return departmentRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(departmentMapper::toDTO);
    }
}
