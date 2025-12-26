package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.base.types.ApplicationStatus;

/**
 * WebSocket通知服务接口
 * 用于实时推送申请状态更新
 */
public interface WebSocketNotificationService {

    /**
     * 发送申请状态更新通知
     * @param application 员工申请
     * @param oldStatus 原状态
     * @param newStatus 新状态
     */
    void sendApplicationStatusUpdate(EmployeeApplication application, ApplicationStatus oldStatus, ApplicationStatus newStatus);

    /**
     * 发送申请转换完成通知
     * @param application 员工申请
     * @param employeeId 新员工ID
     */
    void sendApplicationConversionUpdate(EmployeeApplication application, Long employeeId);

    /**
     * 发送系统通知给所有管理员
     * @param message 通知消息
     */
    void sendSystemNotificationToAdmins(String message);

    /**
     * 发送通知给特定用户
     * @param userId 用户ID
     * @param message 消息内容
     * @param type 消息类型
     */
    void sendNotificationToUser(Long userId, String message, String type);

    /**
     * 发送审核提醒给审核人员
     * @param application 员工申请
     */
    void sendReviewReminderToReviewers(EmployeeApplication application);
}