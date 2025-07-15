package com.fwai.turtle.modules.organization.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeProjectDTO {
    private Long id;
    private Long employeeId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String role;
    private String clientName;
    private String projectScale;
    private String projectDescription;
    private String responsibilities;
    private String technicalStack;
    private String achievements;
    private String projectType;
    private Boolean isInternal;
    private String remarks;
} 