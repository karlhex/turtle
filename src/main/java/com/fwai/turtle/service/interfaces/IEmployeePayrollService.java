package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeePayrollDTO;

public interface IEmployeePayrollService {
    
    /**
     * Add a new employee payroll record
     * @param employeeId the employee ID
     * @param payrollDTO the payroll details
     * @return the created payroll record
     */
    Result<EmployeePayrollDTO> add(Long employeeId, EmployeePayrollDTO payrollDTO);

    /**
     * Update an existing employee payroll record
     * @param employeeId the employee ID
     * @param payrollId the payroll record ID
     * @param payrollDTO the updated payroll details
     * @return the updated payroll record
     */
    Result<EmployeePayrollDTO> update(Long employeeId, Long payrollId, EmployeePayrollDTO payrollDTO);

    /**
     * Delete an employee payroll record
     * @param employeeId the employee ID
     * @param payrollId the payroll record ID
     * @return void result
     */
    Result<Void> delete(Long employeeId, Long payrollId);
}
