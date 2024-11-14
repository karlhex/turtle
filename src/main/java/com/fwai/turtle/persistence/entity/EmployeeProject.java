package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * EmployeeProject entity
 * 员工项目经验表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_projects")
public class EmployeeProject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;          // 关联员工

    @Column(nullable = false)
    private String projectName;         // 项目名称

    @Column(nullable = false)
    private LocalDate startDate;        // 开始日期

    @Column
    private LocalDate endDate;          // 结束日期

    @Column(nullable = false)
    private String role;                // 担任角色

    @Column
    private String clientName;          // 客户名称

    @Column
    private String projectScale;        // 项目规模

    @Column(columnDefinition = "TEXT")
    private String projectDescription;  // 项目描述

    @Column(columnDefinition = "TEXT")
    private String responsibilities;    // 主要职责

    @Column(columnDefinition = "TEXT")
    private String technicalStack;      // 技术栈

    @Column(columnDefinition = "TEXT")
    private String achievements;        // 项目成就

    @Column
    private String projectType;         // 项目类型

    @Column
    @Builder.Default
    private Boolean isInternal = false; // 是否内部项目

    @Column
    private String remarks;             // 备注
} 