package com.fwai.turtle.modules.organization.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import com.fwai.turtle.modules.finance.entity.BankAccount;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fwai.turtle.base.types.PayrollStatus;

/**
 * EmployeePayroll entity
 * 员工薪资表
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "employee_payrolls")
public class EmployeePayroll extends BaseEntity {

    @Column(nullable = false)
    private Long employeeId;
    
    @Column(nullable = false)
    private LocalDateTime payPeriodStart;   // 薪资周期开始

    @Column(nullable = false)
    private LocalDateTime payPeriodEnd;     // 薪资周期结束

    @Column(nullable = false)
    private BigDecimal basicSalary;         // 基本工资

    @Column
    private BigDecimal overtime;            // 加班费

    @Column
    private BigDecimal bonus;               // 奖金

    @Column
    private BigDecimal allowance;           // 补贴

    @Column
    private BigDecimal deductions;          // 扣除项

    @Column(nullable = false)
    private BigDecimal netSalary;           // 实发工资

    @Column
    private String paymentMethod;           // 支付方式

    @OneToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;             // 银行账号

    @Column
    private String remarks;                 // 备注

    @Column
    @Enumerated(EnumType.STRING)
    private PayrollStatus status;           // 发放状态
} 