package com.fwai.turtle.modules.organization.dto;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.base.types.Gender;
import com.fwai.turtle.base.types.IdType;
import com.fwai.turtle.base.types.EmployeeContractType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * EmployeeApplication DTO
 * 员工入职申请数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeApplicationDTO {
    
    private Long id;
    private ApplicationStatus status;
    
    // === 申请人基本信息 ===
    private String name;
    private String email;
    private String phone;
    private LocalDate birthday;
    private Gender gender;
    private String ethnicity;
    private IdType idType;
    private String idNumber;
    private String emergencyContactName;
    private String emergencyContactPhone;
    
    // === 社保公积金信息 ===
    private String socialSecurityNumber;
    private String providentFundNumber;
    private String bankAccount;
    private String bankName;
    
    // === 求职信息 ===
    private String desiredPosition;
    private String expectedSalary;
    private EmployeeContractType preferredContractType;
    private String selfIntroduction;
    private String workExperience;
    private String educationBackground;
    private String certifications;
    
    // === 用户信息 ===
    private Long applicantUserId;
    private String applicantUserName;
    private Long reviewerUserId;
    private String reviewerUserName;
    
    // === 审核信息 ===
    private String reviewComments;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;

    // === 转换信息 ===
    private Boolean convertedToEmployee;
    private Long convertedEmployeeId;
    private LocalDateTime convertedAt;

    // === 时间戳 ===
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 自定义status设置方法，兼容旧的状态值
     */
    @JsonSetter("status")
    public void setStatusFromString(String statusStr) {
        if (statusStr == null || statusStr.isEmpty()) {
            this.status = null;
            return;
        }
        
        // 处理旧的状态值映射
        switch (statusStr.toUpperCase()) {
            case "SUBMITTED":
                this.status = ApplicationStatus.PENDING;
                break;
            case "SUPPLEMENTARY_REQUIRED":
                this.status = ApplicationStatus.UNDER_REVIEW;
                break;
            default:
                try {
                    this.status = ApplicationStatus.valueOf(statusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // 如果无法解析，设置为PENDING作为默认值
                    this.status = ApplicationStatus.PENDING;
                }
                break;
        }
    }
}