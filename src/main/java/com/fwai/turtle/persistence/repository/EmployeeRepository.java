package com.fwai.turtle.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fwai.turtle.persistence.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmployeeNumber(String employeeNumber);
    boolean existsByIdNumber(String idNumber);
} 