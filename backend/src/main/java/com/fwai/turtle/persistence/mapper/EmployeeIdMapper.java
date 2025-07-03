package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.persistence.repository.EmployeeRepository;

import org.springframework.stereotype.Component;

import com.fwai.turtle.persistence.entity.Employee;

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
