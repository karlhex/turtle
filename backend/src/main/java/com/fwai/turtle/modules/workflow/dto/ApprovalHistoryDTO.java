package com.fwai.turtle.modules.workflow.dto;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 审批历史DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprovalHistoryDTO {
    
    private String taskId;              // 任务ID
    private String taskName;            // 任务名称
    private String assignee;            // 审批人
    private String assigneeName;        // 审批人姓名
    private LocalDateTime startTime;    // 开始时间
    private LocalDateTime endTime;      // 结束时间
    private Long durationInMillis;      // 持续时间(毫秒)
    private String decision;            // 审批决定
    private String comments;            // 审批意见
    private String deleteReason;        // 删除原因(如果任务被取消)
    
    // 流程信息
    private String processInstanceId;   // 流程实例ID
    private String processDefinitionKey; // 流程定义键
    private String activityId;          // 活动ID
    private String activityName;        // 活动名称
    
    // 业务信息
    private Long applicationId;         // 关联的申请ID
    private String stepType;            // 步骤类型(HR_REVIEW, DEPT_APPROVAL, GM_APPROVAL等)
    
    /**
     * 格式化持续时间为易读格式
     */
    public String getFormattedDuration() {
        if (durationInMillis == null) {
            return "-";
        }
        
        long seconds = durationInMillis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) {
            return String.format("%d天%d小时", days, hours % 24);
        } else if (hours > 0) {
            return String.format("%d小时%d分钟", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%d分钟", minutes);
        } else {
            return String.format("%d秒", seconds);
        }
    }
    
    /**
     * 获取审批结果的中文描述
     */
    public String getDecisionLabel() {
        if (decision == null) {
            return "处理中";
        }
        
        switch (decision.toUpperCase()) {
            case "APPROVED":
                return "批准";
            case "REJECTED":
                return "拒绝";
            case "VALIDATED":
                return "HR总监已审批";
            case "PENDING":
                return "待处理";
            default:
                return decision;
        }
    }
}