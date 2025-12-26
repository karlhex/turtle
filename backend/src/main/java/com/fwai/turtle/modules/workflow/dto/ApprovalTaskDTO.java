package com.fwai.turtle.modules.workflow.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审批任务DTO
 * 用户待办任务的数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalTaskDTO {
    
    private String id;                      // 任务ID（来自工作流引擎）
    private String name;                    // 任务名称
    private Long applicationId;             // 关联的业务申请ID
    private String applicantName;           // 申请人姓名
    private String applicantEmail;          // 申请人邮箱
    private LocalDateTime createTime;       // 任务创建时间
    private String stepType;                // 步骤类型（HR_REVIEW, MANAGER_APPROVAL等）
    private String requestType;             // 请求类型（EMPLOYEE_APPLICATION, REIMBURSEMENT等）
    private String priority;                // 优先级
    private String description;             // 任务描述
    private String assignee;                // 指定负责人
    private String processInstanceId;       // 工作流实例ID
    private String businessKey;             // 业务键
    private LocalDateTime dueDate;          // 截止日期
    private String formKey;                 // 表单键（用于前端渲染）
    
    // 业务相关信息
    private String departmentName;          // 部门名称
    private Double amount;                  // 金额
    private String currency;                // 货币
    private Object businessData;            // 业务数据
}