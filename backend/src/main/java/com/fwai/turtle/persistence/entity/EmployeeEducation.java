package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * EmployeeEducation entity
 * 员工教育经历表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_educations")
public class EmployeeEducation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
    
    @Column(nullable = false)
    private String school;              // 学校名称

    @Column(nullable = false)
    private String major;               // 专业

    @Column(nullable = false)
    private String degree;              // 学位

    @Column(nullable = false)
    private LocalDate startDate;        // 入学日期

    @Column(nullable = false)
    private LocalDate endDate;          // 毕业日期

    @Column
    private String certificateNumber;    // 证书编号

    @Column
    private Boolean isHighestDegree;    // 是否最高学历

    @Column
    private String remarks;             // 备注
}