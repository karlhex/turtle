package com.fwai.turtle.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import com.fwai.turtle.types.ContractType;
import com.fwai.turtle.types.Gender;
import com.fwai.turtle.types.IdType;

/**
 * Employee entity
 * 员工信息表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;            // 姓名

    @Column(unique = true)
    private String employeeNumber;  // 工号

    @Column
    private String email;           // 邮箱

    @Column
    private String phone;           // 电话

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id")
    private Department department;   // 所属部门

    @Column
    private String position;        // 职位

    @Column
    @Builder.Default
    private Boolean isActive = true; // 是否在职

    @Column
    private LocalDate hireDate; // 入职日期

    @Column
    private LocalDate leaveDate; // 离职日期

    @Column
    private String remarks;         // 备注

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "emergency_contact_id")
    private Person emergencyContact;        // 紧急联系人

    @Column
    private LocalDate birthday;             // 生日

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;                  // 性别

    @Column
    private String ethnicity;               // 民族

    @Column
    @Enumerated(EnumType.STRING)
    private ContractType contractType;      // 合同类型

    @Column
    private Integer contractDuration;       // 合同期限(月)

    @Column
    private LocalDateTime contractStartDate; // 合同开始日期

    @Column
    @Enumerated(EnumType.STRING)
    private IdType idType;                  // 证件类型

    @Column(unique = true)
    private String idNumber;                // 证件号码

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;            // 关联用户账号

    @OneToMany(mappedBy = "employeeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeEducation> educations; // 教育经历

    @OneToMany(mappedBy = "employeeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeAttendance> attendances; // 考勤记录

    @OneToMany(mappedBy = "employeeId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeLeave> leaves; // 请假记录
} 