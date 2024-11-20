package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeeLeaveDTO;

public interface IEmployeeLeaveService {
    
    /**
     * Add a new employee leave record
     * @param employeeId the employee ID
     * @param leaveDTO the leave details
     * @return the created leave record
     */
    Result<EmployeeLeaveDTO> add(Long employeeId, EmployeeLeaveDTO leaveDTO);

    /**
     * Update an existing employee leave record
     * @param employeeId the employee ID
     * @param leaveId the leave record ID
     * @param leaveDTO the updated leave details
     * @return the updated leave record
     */
    Result<EmployeeLeaveDTO> update(Long employeeId, Long leaveId, EmployeeLeaveDTO leaveDTO);

    /**
     * Delete an employee leave record
     * @param employeeId the employee ID
     * @param leaveId the leave record ID
     * @return void result
     */
    Result<Void> delete(Long employeeId, Long leaveId);
}
