package com.fwai.turtle.modules.workflow.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 工作流任务DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowTaskDTO {
    
    private String id;                    // 任务ID
    private String name;                  // 任务名称
    private String description;           // 任务描述
    private String assignee;              // 任务分配人
    private String processInstanceId;     // 流程实例ID
    private String processDefinitionKey;  // 流程定义键
    private LocalDateTime createTime;     // 创建时间
    private LocalDateTime dueDate;        // 到期时间
    private Integer priority;             // 优先级
    private String category;              // 分类
    private String formKey;               // 表单键
    private String executionId;           // 执行ID
    private String processDefinitionName; // 流程定义名称
    private String businessKey;           // 业务键
    
    // 任务变量
    private Map<String, Object> processVariables;  // 流程变量
    private Map<String, Object> taskLocalVariables; // 任务本地变量
    
    // 候选信息
    private java.util.List<String> candidateUsers;   // 候选用户
    private java.util.List<String> candidateGroups;  // 候选组
    
    // 业务相关信息
    private Long applicationId;           // 关联的申请ID（从流程变量中提取）
    private String applicantName;         // 申请人姓名
    private String applicantEmail;        // 申请人邮箱
    private String currentStatus;         // 当前状态
}