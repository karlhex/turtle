package com.fwai.turtle.dto;

import java.time.LocalDate;
import lombok.Data;

/**
 * DTO for Employee Job History
 */
@Data
public class EmployeeJobHistoryDTO {

    private Long id;

    private Long employeeId;

    private String companyName;         // 公司名称

    private String position;            // 职位

    private LocalDate startDate;        // 入职日期

    private LocalDate endDate;          // 离职日期

    private String department;          // 所在部门

    private String jobDescription;      // 工作描述

    private String achievements;        // 主要成就

    private String leavingReason;       // 离职原因

    private String referenceContact;    // 证明人联系方式

    private String remarks;             // 备注
}
