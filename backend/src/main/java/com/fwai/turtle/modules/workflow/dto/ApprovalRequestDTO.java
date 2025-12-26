package com.fwai.turtle.modules.workflow.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审批请求DTO
 * 统一的审批请求数据传输对象，适用于所有业务类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalRequestDTO {
    
    private Long id;                        // 审批请求ID
    private Long applicationId;             // 业务申请ID（如员工申请ID、报销ID等）
    private String applicantName;           // 申请人姓名
    private String applicantEmail;          // 申请人邮箱
    private String requestType;             // 请求类型（EMPLOYEE_APPLICATION, REIMBURSEMENT, CONTRACT等）
    private String status;                  // 当前状态
    private LocalDateTime submitTime;       // 提交时间
    private String currentStep;             // 当前审批步骤
    private String assignee;                // 当前负责人
    private String priority;                // 优先级（LOW, MEDIUM, HIGH）
    private String description;             // 描述
    private String businessKey;             // 业务键（用于工作流关联）
    private String workflowInstanceId;      // 工作流实例ID
    private LocalDateTime createdAt;        // 创建时间
    private LocalDateTime updatedAt;        // 更新时间
    
    // 业务相关字段
    private Object businessData;            // 业务数据（JSON格式）
    private String departmentName;          // 部门名称
    private Double amount;                  // 金额（适用于报销、合同等）
    private String currency;                // 货币类型
}