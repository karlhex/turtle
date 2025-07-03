package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;
import com.fwai.turtle.types.ProjectStatus;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {

    @Column(name = "project_name", nullable = false)
    private String projectName;  // 项目名称

    @Column(name = "project_no", nullable = false, unique = true)
    private String projectNo;  // 项目编号

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;  // 开始日期

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;  // 结束日期

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;  // 项目状态

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Employee manager;  // 项目负责员工

    @Column(name = "remarks")
    private String remarks;  // 备注

    @OneToMany(mappedBy = "projectId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts = new ArrayList<>();  // 关联的合同列表
}
