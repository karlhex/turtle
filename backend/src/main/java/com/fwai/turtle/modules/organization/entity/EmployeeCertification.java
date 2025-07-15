package com.fwai.turtle.modules.organization.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;

/**
 * EmployeeCertification entity
 * 员工证书表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_certifications")
public class EmployeeCertification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long employeeId;
    
    @Column(nullable = false)
    private String certificationName;   // 证书名称

    @Column(nullable = false)
    private String certificationNumber; // 证书编号

    @Column(nullable = false)
    private String issuingAuthority;    // 发证机构

    @Column(nullable = false)
    private LocalDate issueDate;        // 发证日期

    @Column
    private LocalDate expiryDate;       // 到期日期

    @Column
    private String certificationLevel;   // 证书等级

    @Column
    private String certificationField;   // 证书领域/类别

    @Column
    @Builder.Default
    private Boolean isValid = true;     // 是否有效

    @Column
    private String attachmentUrl;       // 证书附件URL

    @Column
    private String remarks;             // 备注
} 