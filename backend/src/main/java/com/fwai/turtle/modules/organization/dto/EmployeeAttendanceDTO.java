package com.fwai.turtle.modules.organization.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record EmployeeAttendanceDTO(
    Long id,
    Long employeeId,
    
    @NotNull(message = "考勤日期不能为空")
    LocalDate attendanceDate,
    
    LocalTime checkInTime,
    LocalTime checkOutTime,
    String status,
    String remarks
) {
    public EmployeeAttendanceDTO {
        if (attendanceDate == null) {
            throw new IllegalArgumentException("考勤日期不能为空");
        }
    }
} 