package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeeEducationDTO;

public interface IEmployeeEducationService {
    
    /**
     * Add a new employee education record
     * @param employeeId the employee ID
     * @param educationDTO the education details
     * @return the created education record
     */
    Result<EmployeeEducationDTO> add(Long employeeId, EmployeeEducationDTO educationDTO);

    /**
     * Update an existing employee education record
     * @param employeeId the employee ID
     * @param educationId the education record ID
     * @param educationDTO the updated education details
     * @return the updated education record
     */
    Result<EmployeeEducationDTO> update(Long employeeId, Long educationId, EmployeeEducationDTO educationDTO);

    /**
     * Delete an employee education record
     * @param employeeId the employee ID
     * @param educationId the education record ID
     * @return void result
     */
    Result<Void> delete(Long employeeId, Long educationId);
}
