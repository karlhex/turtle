package com.fwai.turtle.modules.organization.service;

/**
 * ReviewTimeoutService
 * 审核超时监控服务接口
 */
public interface ReviewTimeoutService {

    /**
     * 检查并处理超时的申请
     * 扫描所有待审核状态且超过指定时间的申请，发送提醒通知
     */
    void checkAndHandleTimeoutApplications();

    /**
     * 为特定申请设置超时提醒
     * @param applicationId 申请ID
     * @param timeoutHours 超时小时数
     */
    void scheduleTimeoutReminder(Long applicationId, int timeoutHours);

    /**
     * 取消申请的超时提醒
     * @param applicationId 申请ID
     */
    void cancelTimeoutReminder(Long applicationId);

    /**
     * 检查单个申请是否超时
     * @param applicationId 申请ID
     * @return true 如果申请已超时
     */
    boolean isApplicationTimeout(Long applicationId);

    /**
     * 获取默认审核超时时间（小时）
     * @return 默认超时小时数
     */
    int getDefaultTimeoutHours();
}