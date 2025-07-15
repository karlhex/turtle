package com.fwai.turtle.modules.organization.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * EmployeeJobHistory entity
 * 员工工作经历表
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "employee_job_histories")
public class EmployeeJobHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private String companyName;         // 公司名称

    @Column(nullable = false)
    private String position;            // 职位

    @Column(nullable = false)
    private LocalDate startDate;        // 入职日期

    @Column
    private LocalDate endDate;          // 离职日期

    @Column
    private String department;          // 所在部门

    @Column
    private String jobDescription;      // 工作描述

    @Column
    private String achievements;        // 主要成就

    @Column
    private String leavingReason;       // 离职原因

    @Column
    private String referenceContact;    // 证明人联系方式

    @Column
    private String remarks;             // 备注
} 