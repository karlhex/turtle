package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.modules.organization.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket通知服务实现类
 * 用于实时推送申请状态更新
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationServiceImpl implements WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void sendApplicationStatusUpdate(EmployeeApplication application, ApplicationStatus oldStatus, ApplicationStatus newStatus) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "APPLICATION_STATUS_UPDATE");
            message.put("applicationId", application.getId());
            message.put("applicantName", application.getName());
            message.put("oldStatus", oldStatus != null ? oldStatus.name() : null);
            message.put("newStatus", newStatus.name());
            message.put("newStatusDescription", getStatusDescription(newStatus));
            message.put("updatedAt", LocalDateTime.now().format(DATE_FORMATTER));
            message.put("message", String.format("申请 %s 的状态已从 %s 更新为 %s",
                application.getName(),
                oldStatus != null ? getStatusDescription(oldStatus) : "未知",
                getStatusDescription(newStatus)));

            // 发送给申请人
            if (application.getApplicantUserId() != null) {
                messagingTemplate.convertAndSendToUser(
                    application.getApplicantUserId().toString(),
                    "/queue/notifications",
                    message
                );
            }

            // 发送给管理员/HR
            messagingTemplate.convertAndSend("/topic/admin/applications", message);

            log.info("已发送申请状态更新WebSocket通知: {} -> {}", oldStatus, newStatus);

        } catch (Exception e) {
            log.error("发送申请状态更新WebSocket通知失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendApplicationConversionUpdate(EmployeeApplication application, Long employeeId) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "APPLICATION_CONVERSION_COMPLETED");
            message.put("applicationId", application.getId());
            message.put("applicantName", application.getName());
            message.put("employeeId", employeeId);
            message.put("convertedAt", LocalDateTime.now().format(DATE_FORMATTER));
            message.put("message", String.format("申请 %s 已成功转换为员工记录，员工ID: %s",
                application.getName(), employeeId));

            // 发送给申请人
            if (application.getApplicantUserId() != null) {
                messagingTemplate.convertAndSendToUser(
                    application.getApplicantUserId().toString(),
                    "/queue/notifications",
                    message
                );
            }

            // 发送给管理员/HR
            messagingTemplate.convertAndSend("/topic/admin/applications", message);

            log.info("已发送申请转换完成WebSocket通知: applicationId={}, employeeId={}",
                application.getId(), employeeId);

        } catch (Exception e) {
            log.error("发送申请转换完成WebSocket通知失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendSystemNotificationToAdmins(String messageContent) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "SYSTEM_NOTIFICATION");
            message.put("message", messageContent);
            message.put("timestamp", LocalDateTime.now().format(DATE_FORMATTER));

            messagingTemplate.convertAndSend("/topic/admin/system", message);

            log.info("已发送系统通知: {}", messageContent);

        } catch (Exception e) {
            log.error("发送系统通知失败: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendNotificationToUser(Long userId, String messageContent, String type) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", type);
            message.put("message", messageContent);
            message.put("timestamp", LocalDateTime.now().format(DATE_FORMATTER));

            messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications",
                message
            );

            log.info("已发送通知给用户 {}: {}", userId, messageContent);

        } catch (Exception e) {
            log.error("发送用户通知失败: userId={}, error={}", userId, e.getMessage(), e);
        }
    }

    @Override
    public void sendReviewReminderToReviewers(EmployeeApplication application) {
        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "REVIEW_REMINDER");
            message.put("applicationId", application.getId());
            message.put("applicantName", application.getName());
            message.put("submittedAt", application.getSubmittedAt() != null ?
                application.getSubmittedAt().format(DATE_FORMATTER) : "未知");
            message.put("message", String.format("申请 %s 需要审核处理，请及时处理！", application.getName()));
            message.put("timestamp", LocalDateTime.now().format(DATE_FORMATTER));

            // 发送给所有审核人员（这里发送到管理员频道）
            messagingTemplate.convertAndSend("/topic/admin/review-reminders", message);

            log.info("已发送审核提醒: applicationId={}, applicantName={}",
                application.getId(), application.getName());

        } catch (Exception e) {
            log.error("发送审核提醒失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 获取状态描述
     */
    private String getStatusDescription(ApplicationStatus status) {
        if (status == null) {
            return "未知";
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
}