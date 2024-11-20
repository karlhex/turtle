package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.common.Result;

import java.util.List;

public interface EmployeeAttendanceService {

    Result<EmployeeAttendanceDTO> add(Long employeeId, EmployeeAttendanceDTO attendanceDTO);

    Result<EmployeeAttendanceDTO> update(Long employeeId, Long attendanceId, 
            EmployeeAttendanceDTO attendanceDTO);

    Result<List<EmployeeAttendanceDTO>> getByEmployeeId(Long employeeId);

    Result<Void> delete(Long employeeId, Long attendanceId);

}
