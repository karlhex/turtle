package com.fwai.turtle.modules.organization.mapper;

import com.fwai.turtle.modules.organization.repository.EmployeeRepository;

import org.springframework.stereotype.Component;

import com.fwai.turtle.modules.organization.entity.Employee;

@Component
public class EmployeeIdMapper {
    private final EmployeeRepository employeeRepository;

    public EmployeeIdMapper(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee fromId(Long id) {
        if (id == null) return null;
        return employeeRepository.findById(id).orElse(null);
    }
}
