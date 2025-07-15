package com.fwai.turtle.modules.organization.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.modules.organization.entity.Department;
import com.fwai.turtle.modules.organization.repository.DepartmentRepository;

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
