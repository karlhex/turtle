package com.fwai.turtle.persistence.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.persistence.entity.Department;
import com.fwai.turtle.persistence.repository.DepartmentRepository;

@Component
public class DepartmentIdMapper {

    private final DepartmentRepository departmentRepository;

    public DepartmentIdMapper(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department fromId(Long id) {
        if (id == null) return null;
        Department department = departmentRepository.findById(id).orElse(null);

        return department;
    }
}
