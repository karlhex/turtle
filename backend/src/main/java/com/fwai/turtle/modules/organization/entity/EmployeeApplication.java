package com.fwai.turtle.modules.organization.entity;

import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.base.types.Gender;
import com.fwai.turtle.base.types.IdType;
import com.fwai.turtle.base.types.EmployeeContractType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * EmployeeApplication entity
 * 员工入职申请表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_applications")
public class EmployeeApplication {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.PENDING; // 申请状态
    
    // === 申请人基本信息 ===
    @Column(nullable = false)
    private String name;                    // 姓名
    
    @Column(nullable = false)
    private String email;                   // 邮箱
    
    @Column
    private String phone;                   // 电话
    
    @Column
    private LocalDate birthday;             // 生日
    
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;                  // 性别
    
    @Column
    private String ethnicity;               // 民族
    
    @Column
    @Enumerated(EnumType.STRING)
    private IdType idType;                  // 证件类型
    
    @Column(nullable = false)
    private String idNumber;                // 证件号码
    
    @Column
    private String emergencyContactName;    // 紧急联系人姓名
    
    @Column
    private String emergencyContactPhone;   // 紧急联系人电话
    
    // === 社保公积金信息 ===
    @Column
    private String socialSecurityNumber;    // 社保号
    
    @Column 
    private String providentFundNumber;     // 公积金账号
    
    @Column
    private String bankAccount;             // 银行账号
    
    @Column
    private String bankName;                // 开户银行
    
    // === 求职信息 ===
    @Column
    private String desiredPosition;         // 期望职位
    
    @Column
    private String expectedSalary;          // 期望薪资
    
    @Column
    @Enumerated(EnumType.STRING)
    private EmployeeContractType preferredContractType; // 期望合同类型
    
    @Column(columnDefinition = "TEXT")
    private String selfIntroduction;        // 自我介绍
    
    @Column(columnDefinition = "TEXT") 
    private String workExperience;          // 工作经验描述
    
    @Column(columnDefinition = "TEXT")
    private String educationBackground;     // 学历背景描述
    
    @Column(columnDefinition = "TEXT")
    private String certifications;          // 证书信息
    
    // === 关联用户 ===
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_user_id", nullable = false)
    private User applicantUser;             // GUEST用户（申请人）
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_user_id")
    private User reviewerUser;              // 人力专员（审核人）
    
    // === 审核信息 ===
    @Column(columnDefinition = "TEXT")
    private String reviewComments;          // 审核意见
    
    @Column
    private LocalDateTime submittedAt;      // 提交时间
    
    @Column
    private LocalDateTime reviewedAt;       // 审核时间
    
    // === 工作流信息 ===
    @Column(name = "workflow_instance_id")
    private String workflowInstanceId;      // 工作流实例ID
    
    // === 转换信息 ===
    @Column(name = "converted_to_employee", nullable = false)
    @Builder.Default
    private boolean convertedToEmployee = false;    // 是否已转换为员工
    
    @Column(name = "converted_employee_id")
    private Long convertedEmployeeId;               // 转换后的员工ID
    
    @Column(name = "converted_at")
    private LocalDateTime convertedAt;              // 转换时间
    
    // === 时间戳 ===
    @Column
    private LocalDateTime createdAt;        // 创建时间
    
    @Column
    private LocalDateTime updatedAt;        // 更新时间
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.submittedAt == null && this.status == ApplicationStatus.PENDING) {
            this.submittedAt = now;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.reviewedAt == null && 
            (this.status == ApplicationStatus.APPROVED || 
             this.status == ApplicationStatus.REJECTED || 
             this.status == ApplicationStatus.VALIDATED)) {
            this.reviewedAt = LocalDateTime.now();
        }
    }
    
    /**
     * 获取申请人用户ID
     * @return 申请人用户ID
     */
    public Long getApplicantUserId() {
        return this.applicantUser != null ? this.applicantUser.getId() : null;
    }
}