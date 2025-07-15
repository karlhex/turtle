package com.fwai.turtle.modules.organization.dto;

import com.fwai.turtle.base.types.PayrollStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class EmployeePayrollDTO {
    private Long id;
    private Long employeeId;
    private LocalDateTime payPeriodStart;
    private LocalDateTime payPeriodEnd;
    private BigDecimal basicSalary;
    private BigDecimal overtime;
    private BigDecimal bonus;
    private BigDecimal allowance;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private String paymentMethod;
    private Long bankAccountId;
    private String remarks;
    private PayrollStatus status;
} 