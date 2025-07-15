package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.DepartmentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {
    Page<DepartmentDTO> findAll(Pageable pageable);
    DepartmentDTO findById(Long id);
    DepartmentDTO create(DepartmentDTO departmentDTO);
    DepartmentDTO update(Long id, DepartmentDTO departmentDTO);
    void delete(Long id);
    Page<DepartmentDTO> search(String query, Pageable pageable);
}
