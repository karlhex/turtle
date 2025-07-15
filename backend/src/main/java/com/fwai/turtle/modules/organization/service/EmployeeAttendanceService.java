package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.EmployeeAttendanceDTO;
import java.util.List;

public interface EmployeeAttendanceService {

    EmployeeAttendanceDTO add(Long employeeId, EmployeeAttendanceDTO attendanceDTO);

    EmployeeAttendanceDTO update(Long employeeId, Long attendanceId, 
            EmployeeAttendanceDTO attendanceDTO);

    List<EmployeeAttendanceDTO> getByEmployeeId(Long employeeId);

    void delete(Long employeeId, Long attendanceId);

}
