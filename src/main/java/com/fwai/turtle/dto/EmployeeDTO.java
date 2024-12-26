package com.fwai.turtle.dto;

import lombok.Data;
import jakarta.validation.constraints.*;

import java.util.List;

import com.fwai.turtle.persistence.entity.Department;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.types.EmployeeStatus;

import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String employeeNumber;
    
    @Email
    private String email;
    
    private String phone;
    private Department department;
    private String position;
    @NotNull
    private EmployeeStatus status;

    private LocalDate hireDate;

    private LocalDate leaveDate;

    private String remarks;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private LocalDate birthday;
    private String gender;
    private String ethnicity;
    private String contractType;
    private Integer contractDuration;
    private LocalDate contractStartDate;
    private String idType;
    
    private User user;

    @NotBlank
    private String idNumber;

    private List<EmployeeEducationDTO> educations;
    private List<EmployeeAttendanceDTO> attendances;
    private List<EmployeeLeaveDTO> leaves;
    private List<EmployeeJobHistoryDTO> jobHistories;
} 