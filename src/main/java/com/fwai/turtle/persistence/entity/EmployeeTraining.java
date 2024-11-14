package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import com.fwai.turtle.types.TrainingStatus;
import com.fwai.turtle.types.TrainingType;

/**
 * EmployeeTraining entity
 * 员工培训记录表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_trainings")
public class EmployeeTraining {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;              // 关联员工

    @Column(nullable = false)
    private String trainingName;            // 培训名称

    @Column(nullable = false)
    private LocalDateTime startTime;        // 开始时间

    @Column(nullable = false)
    private LocalDateTime endTime;          // 结束时间

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainingType type;              // 培训类型

    @Column
    private String trainer;                 // 培训讲师

    @Column
    private String location;                // 培训地点

    @Column
    private String description;             // 培训描述

    @Column
    private String attachments;             // 培训材料附件

    @Column
    private Integer score;                  // 培训成绩

    @Column
    @Enumerated(EnumType.STRING)
    private TrainingStatus status;          // 培训状态

    @Column
    private String remarks;                 // 备注
} 