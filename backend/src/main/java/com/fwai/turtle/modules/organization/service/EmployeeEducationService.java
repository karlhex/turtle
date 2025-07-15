package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.EmployeeEducationDTO;
import java.util.List;

public interface EmployeeEducationService {
    
    /**
     * Add a new employee education record
     * @param employeeId the employee ID
     * @param educationDTO the education details
     * @return the created education record
     */
    EmployeeEducationDTO add(Long employeeId, EmployeeEducationDTO educationDTO);

    /**
     * Update an existing employee education record
     * @param employeeId the employee ID
     * @param educationId the education record ID
     * @param educationDTO the updated education details
     * @return the updated education record
     */
    EmployeeEducationDTO update(Long employeeId, Long educationId, EmployeeEducationDTO educationDTO);

    /**
     * Delete an employee education record
     * @param employeeId the employee ID
     * @param educationId the education record ID
     */
    void delete(Long employeeId, Long educationId);

    /**
     * Get all education records for an employee
     * @param employeeId the employee ID
     * @return list of education records
     */
    List<EmployeeEducationDTO> getByEmployeeId(Long employeeId);
}
