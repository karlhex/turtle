package com.fwai.turtle.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.persistence.entity.EmployeePayroll;

import java.util.Optional;

@Repository
public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, Long> {
    /**
     * Find a payroll record by its ID and employee ID
     * @param id the payroll record ID
     * @param employeeId the employee ID
     * @return Optional of EmployeePayroll
     */
    Optional<EmployeePayroll> findByIdAndEmployeeId(Long id, Long employeeId);

    /**
     * Check if a payroll record exists by its ID and employee ID
     * @param id the payroll record ID
     * @param employeeId the employee ID
     * @return true if exists, false otherwise
     */
    boolean existsByIdAndEmployeeId(Long id, Long employeeId);
}
