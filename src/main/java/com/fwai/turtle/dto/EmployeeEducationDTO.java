package com.fwai.turtle.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEducationDTO {
    private Long id;
    private Long employeeId;
    private String school;
    private String major;
    private String degree;
    
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    private String certificateNumber;
    private Boolean isHighestDegree;
    private String remarks;
    
} 