package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

import com.fwai.turtle.types.AttendanceStatus;
import com.fwai.turtle.types.LeaveType;

/**
 * EmployeeLeave entity
 * 员工请假记录表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_leaves")
public class EmployeeLeave {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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