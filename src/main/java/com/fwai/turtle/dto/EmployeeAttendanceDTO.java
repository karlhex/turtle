package com.fwai.turtle.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EmployeeAttendanceDTO {
    private Long id;
    private Long employeeId;
    
    @NotNull(message = "考勤日期不能为空")
    private LocalDate attendanceDate;
    
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private String status;
    private String remarks;
} 