package com.fwai.turtle.modules.organization.dto;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.entity.ApplicationHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * ApplicationHistoryDTO
 * 申请操作历史记录数据传输对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationHistoryDTO {

    private Long id;
    private Long applicationId;
    private ApplicationHistory.ActionType actionType;
    private String actionTypeDescription;
    private ApplicationStatus fromStatus;
    private String fromStatusDescription;
    private ApplicationStatus toStatus;
    private String toStatusDescription;
    private Long operatorId;
    private String operatorName;
    private String description;
    private String details;
    private String workflowTaskId;
    private LocalDateTime createdAt;
    private String clientIp;
    private String userAgent;

    /**
     * 获取操作类型描述
     */
    public String getActionTypeDescription() {
        return actionType != null ? actionType.getDescription() : null;
    }

    /**
     * 获取原状态描述
     */
    public String getFromStatusDescription() {
        return getStatusDescription(fromStatus);
    }

    /**
     * 获取目标状态描述
     */
    public String getToStatusDescription() {
        return getStatusDescription(toStatus);
    }

    /**
     * 获取状态描述
     */
    private String getStatusDescription(ApplicationStatus status) {
        if (status == null) {
            return null;
        }

        switch (status) {
            case PENDING:
                return "待审核";
            case UNDER_REVIEW:
                return "审核中";
            case VALIDATED:
                return "HR总监已审批";
            case APPROVED:
                return "已通过";
            case REJECTED:
                return "已拒绝";
            default:
                return status.name();
        }
    }

    /**
     * 获取操作摘要
     */
    public String getOperationSummary() {
        StringBuilder summary = new StringBuilder();

        if (operatorName != null) {
            summary.append(operatorName);
        } else {
            summary.append("系统");
        }

        summary.append(" ");

        if (actionType != null) {
            summary.append(actionType.getDescription());
        }

        if (fromStatus != null && toStatus != null) {
            summary.append("，状态从 ")
                   .append(getFromStatusDescription())
                   .append(" 变更为 ")
                   .append(getToStatusDescription());
        }

        return summary.toString();
    }
}