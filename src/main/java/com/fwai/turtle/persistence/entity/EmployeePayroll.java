package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fwai.turtle.types.PayrollStatus;

/**
 * EmployeePayroll entity
 * 员工薪资表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_payrolls")
public class EmployeePayroll {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;              // 关联员工

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