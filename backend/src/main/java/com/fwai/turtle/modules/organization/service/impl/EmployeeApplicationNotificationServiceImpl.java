package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.base.service.EmailService;
import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationNotificationService;
import com.fwai.turtle.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * EmployeeApplicationNotificationServiceImpl
 * 员工申请通知服务实现类
 */
@Service
public class EmployeeApplicationNotificationServiceImpl implements EmployeeApplicationNotificationService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Value("${app.frontend.url:http://localhost:4200}")
    private String frontendUrl;

    @Value("${app.hr.email:hr@company.com}")
    private String hrEmail;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public void sendApplicationStatusNotification(EmployeeApplication application, ApplicationStatus oldStatus, ApplicationStatus newStatus) {
        if (application.getApplicantUserId() == null) {
            return;
        }

        String applicantEmail = getUserEmail(application.getApplicantUserId());
        if (applicantEmail == null) {
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("applicantName", application.getName());
        variables.put("applicationId", application.getId());
        variables.put("oldStatus", getStatusDisplayName(oldStatus));
        variables.put("newStatus", getStatusDisplayName(newStatus));
        variables.put("frontendUrl", frontendUrl);
        variables.put("submittedAt", application.getSubmittedAt() != null ?
            application.getSubmittedAt().format(DATE_FORMATTER) : "未知");

        String subject = String.format("入职申请状态更新 - %s", application.getName());

        try {
            emailService.sendTemplateEmail(applicantEmail, subject, "employee-application-status-change", variables);
        } catch (Exception e) {
            System.err.println("Failed to send status notification email: " + e.getMessage());
        }
    }

    @Override
    public void sendApplicationSubmittedNotification(EmployeeApplication application) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicantName", application.getName());
        variables.put("applicationId", application.getId());
        variables.put("email", application.getEmail());
        variables.put("phone", application.getPhone());
        variables.put("desiredPosition", application.getDesiredPosition());
        variables.put("frontendUrl", frontendUrl);
        variables.put("submittedAt", application.getSubmittedAt() != null ?
            application.getSubmittedAt().format(DATE_FORMATTER) : "未知");

        String subject = String.format("新的入职申请 - %s", application.getName());

        try {
            emailService.sendTemplateEmail(hrEmail, subject, "employee-application-submitted", variables);
        } catch (Exception e) {
            System.err.println("Failed to send submission notification email: " + e.getMessage());
        }
    }

    @Override
    public void sendReviewTimeoutReminder(EmployeeApplication application) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicantName", application.getName());
        variables.put("applicationId", application.getId());
        variables.put("submittedAt", application.getSubmittedAt() != null ?
            application.getSubmittedAt().format(DATE_FORMATTER) : "未知");
        variables.put("frontendUrl", frontendUrl);

        String subject = String.format("入职申请审核超时提醒 - %s", application.getName());

        try {
            emailService.sendTemplateEmail(hrEmail, subject, "employee-application-timeout", variables);
        } catch (Exception e) {
            System.err.println("Failed to send timeout reminder email: " + e.getMessage());
        }
    }

    @Override
    public void sendConversionCompletedNotification(EmployeeApplication application, Long employeeId) {
        if (application.getApplicantUserId() == null) {
            return;
        }

        String applicantEmail = getUserEmail(application.getApplicantUserId());
        if (applicantEmail == null) {
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("applicantName", application.getName());
        variables.put("employeeId", employeeId);
        variables.put("applicationId", application.getId());
        variables.put("frontendUrl", frontendUrl);
        variables.put("convertedAt", application.getConvertedAt() != null ?
            application.getConvertedAt().format(DATE_FORMATTER) : "未知");

        String subject = String.format("入职申请已转换为员工记录 - %s", application.getName());

        try {
            emailService.sendTemplateEmail(applicantEmail, subject, "employee-application-converted", variables);
        } catch (Exception e) {
            System.err.println("Failed to send conversion notification email: " + e.getMessage());
        }
    }

    @Override
    public void sendApplicationRejectedNotification(EmployeeApplication application, String reason) {
        if (application.getApplicantUserId() == null) {
            return;
        }

        String applicantEmail = getUserEmail(application.getApplicantUserId());
        if (applicantEmail == null) {
            return;
        }

        Map<String, Object> variables = new HashMap<>();
        variables.put("applicantName", application.getName());
        variables.put("applicationId", application.getId());
        variables.put("reason", reason != null ? reason : "未提供具体原因");
        variables.put("frontendUrl", frontendUrl);
        variables.put("reviewedAt", application.getReviewedAt() != null ?
            application.getReviewedAt().format(DATE_FORMATTER) : "未知");

        String subject = String.format("入职申请被拒绝 - %s", application.getName());

        try {
            emailService.sendTemplateEmail(applicantEmail, subject, "employee-application-rejected", variables);
        } catch (Exception e) {
            System.err.println("Failed to send rejection notification email: " + e.getMessage());
        }
    }

    private String getUserEmail(Long userId) {
        try {
            var userOpt = userService.findById(userId);
            return userOpt.map(user -> user.getEmail()).orElse(null);
        } catch (Exception e) {
            System.err.println("Failed to get user email for userId: " + userId);
            return null;
        }
    }

    private String getStatusDisplayName(ApplicationStatus status) {
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