package com.fwai.turtle.dto;

import java.time.LocalDate;
import lombok.Data;

/**
 * DTO for Employee Leave
 */
@Data
public class EmployeeLeaveDTO {

    private Long id;

    private Long employeeId;

    private LocalDate startTime;       // 开始时间

    private LocalDate endTime;         // 结束时间

    private String type;               // 请假类型

    private String reason;             // 请假原因

    private String attachments;        // 附件

    private String approver;           // 审批人

    private String status;             // 状态

    private String remarks;            // 备注
}