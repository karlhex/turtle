package com.fwai.turtle.modules.organization.entity;

import com.fwai.turtle.base.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fwai.turtle.base.types.EmployeeContractType;
import com.fwai.turtle.base.types.EmployeeStatus;
import com.fwai.turtle.base.types.Gender;
import com.fwai.turtle.base.types.IdType;

/**
 * Employee entity
 * 员工信息表
 */
@Data
@ToString(exclude = {"user", "educations", "attendances", "leaves", "jobHistories", "department", "position"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee {
    public Employee(Long id) {
        this.id = id;
    }

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "position_id")
    private Position position;        // 职位

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EmployeeStatus status = EmployeeStatus.APPLICATION; // 员工状态

    @Column
    private LocalDate hireDate; // 入职日期

    @Column
    private LocalDate leaveDate; // 离职日期

    @Column
    private String remarks;         // 备注

    @Column
    private String emergencyContactName;    // 紧急联系人姓名

    @Column
    private String emergencyContactPhone;   // 紧急联系人电话

    @Column
    private LocalDate birthday;             // 生日

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;                  // 性别

    @Column
    private String ethnicity;               // 民族

    @Column
    @Enumerated(EnumType.STRING)
    private EmployeeContractType contractType;      // 合同类型

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
    @JsonIgnoreProperties({"employee", "roles"})
    private User user;            // 关联用户账号

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeEducation> educations; // 教育经历

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeAttendance> attendances; // 考勤记录

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeLeave> leaves; // 请假记录

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeJobHistory> jobHistories;

} 