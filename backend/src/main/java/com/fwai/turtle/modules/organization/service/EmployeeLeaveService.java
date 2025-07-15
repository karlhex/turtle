package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.EmployeeLeaveDTO;

public interface EmployeeLeaveService {
    
    /**
     * Add a new employee leave record
     * @param employeeId the employee ID
     * @param leaveDTO the leave details
     * @return the created leave record
     */
    EmployeeLeaveDTO add(Long employeeId, EmployeeLeaveDTO leaveDTO);

    /**
     * Update an existing employee leave record
     * @param employeeId the employee ID
     * @param leaveId the leave record ID
     * @param leaveDTO the updated leave details
     * @return the updated leave record
     */
    EmployeeLeaveDTO update(Long employeeId, Long leaveId, EmployeeLeaveDTO leaveDTO);

    /**
     * Delete an employee leave record
     * @param employeeId the employee ID
     * @param leaveId the leave record ID
     */
    void delete(Long employeeId, Long leaveId);
}
