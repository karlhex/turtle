package com.fwai.turtle.modules.organization.entity;

import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.types.ApplicationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * ApplicationHistory
 * 申请操作历史记录实体
 */
@Entity
@Table(name = "application_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的申请
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    private EmployeeApplication application;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private ActionType actionType;

    /**
     * 操作前状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "from_status")
    private ApplicationStatus fromStatus;

    /**
     * 操作后状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "to_status")
    private ApplicationStatus toStatus;

    /**
     * 操作人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operator_id")
    private User operator;

    /**
     * 操作描述/备注
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 操作详情（JSON格式存储额外信息）
     */
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    /**
     * 工作流任务ID（如果是工作流操作）
     */
    @Column(name = "workflow_task_id")
    private String workflowTaskId;

    /**
     * 操作时间
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /**
     * 客户端IP地址
     */
    @Column(name = "client_ip")
    private String clientIp;

    /**
     * 用户代理
     */
    @Column(name = "user_agent")
    private String userAgent;

    /**
     * 操作类型枚举
     */
    public enum ActionType {
        CREATED("申请创建"),
        SUBMITTED("申请提交"),
        REVIEWED("申请审核"),
        APPROVED("申请通过"),
        REJECTED("申请拒绝"),
        UPDATED("申请更新"),
        CONVERTED("转换为员工"),
        WORKFLOW_STARTED("工作流启动"),
        WORKFLOW_COMPLETED("工作流完成"),
        STATUS_CHANGED("状态变更"),
        COMMENT_ADDED("添加评论"),
        DOCUMENT_UPLOADED("文档上传"),
        DOCUMENT_DELETED("文档删除"),
        NOTIFICATION_SENT("通知发送"),
        TIMEOUT_REMINDER("超时提醒"),
        SYSTEM_ACTION("系统操作");

        private final String description;

        ActionType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}