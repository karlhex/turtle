package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.base.types.ApplicationStatus;

/**
 * EmployeeApplicationNotificationService
 * 员工申请通知服务接口
 */
public interface EmployeeApplicationNotificationService {

    /**
     * 发送申请状态变化通知
     * @param application 员工申请
     * @param oldStatus 原状态
     * @param newStatus 新状态
     */
    void sendApplicationStatusNotification(EmployeeApplication application, ApplicationStatus oldStatus, ApplicationStatus newStatus);

    /**
     * 发送申请提交通知给审核人员
     * @param application 员工申请
     */
    void sendApplicationSubmittedNotification(EmployeeApplication application);

    /**
     * 发送审核超时提醒
     * @param application 员工申请
     */
    void sendReviewTimeoutReminder(EmployeeApplication application);

    /**
     * 发送转换完成通知
     * @param application 员工申请
     * @param employeeId 新员工ID
     */
    void sendConversionCompletedNotification(EmployeeApplication application, Long employeeId);

    /**
     * 发送申请被拒绝通知
     * @param application 员工申请
     * @param reason 拒绝原因
     */
    void sendApplicationRejectedNotification(EmployeeApplication application, String reason);
}