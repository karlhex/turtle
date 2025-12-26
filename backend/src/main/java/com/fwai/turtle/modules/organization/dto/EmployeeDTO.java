package com.fwai.turtle.modules.organization.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.*;

import java.util.List;
import java.time.LocalDateTime;

import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.types.EmployeeStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String employeeNumber;
    
    @Email
    private String email;
    
    private String phone;
    private Long departmentId;
    private Long positionId;
    private DepartmentDTO department;
    private PositionDTO position;
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
    private LocalDateTime contractStartDate;
    private String idType;
    
    private User user;

    @NotBlank
    private String idNumber;
    
    private String socialSecurityNumber;    // 社保号
    private String providentFundNumber;     // 公积金账号
    private String bankAccount;             // 银行账号
    private String bankName;                // 开户银行

    private List<EmployeeEducationDTO> educations;
    private List<EmployeeAttendanceDTO> attendances;
    private List<EmployeeLeaveDTO> leaves;
    private List<EmployeeJobHistoryDTO> jobHistories;
    
    // 角色信息，用于创建员工时设置用户角色
    private List<Long> roles;
} 