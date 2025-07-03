package com.fwai.turtle.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Department entity
 * 部门信息表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;        // 部门名称

    @Column
    private String description; // 部门描述

    @Column
    private String code;        // 部门编码

    @Column
    @Builder.Default
    private Boolean isActive = true;  // 是否激活

    @Column
    private Long parentId;      // 父部门ID，用于构建部门层级关系

    @Column
    private Long managerId;    // 部门管理人员
} 