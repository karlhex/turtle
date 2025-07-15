package com.fwai.turtle.modules.organization.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.fwai.turtle.base.types.AttendanceStatus;
import com.fwai.turtle.base.types.LeaveType;

/**
 * EmployeeLeave entity
 * 员工请假记录表
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "employee_leaves")
public class EmployeeLeave extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime startTime;        // 请假开始时间

    @Column(nullable = false)
    private LocalDateTime endTime;          // 请假结束时间

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LeaveType type;                 // 请假类型

    @Column(nullable = false)
    private String reason;                  // 请假原因

    @Column
    private String attachments;             // 附件链接

    @Column
    private String approver;                // 审批人

    @Column
    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;        // 审批状态

    @Column
    private String remarks;                 // 备注
} 