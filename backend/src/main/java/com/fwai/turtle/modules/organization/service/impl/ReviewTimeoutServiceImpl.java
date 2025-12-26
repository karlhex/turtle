package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.modules.organization.repository.EmployeeApplicationRepository;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationNotificationService;
import com.fwai.turtle.modules.organization.service.ReviewTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * ReviewTimeoutServiceImpl
 * 审核超时监控服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewTimeoutServiceImpl implements ReviewTimeoutService {

    private final EmployeeApplicationRepository applicationRepository;
    private final EmployeeApplicationNotificationService notificationService;

    @Value("${app.review.timeout.hours:72}")
    private int defaultTimeoutHours;

    @Value("${app.review.timeout.check.enabled:true}")
    private boolean timeoutCheckEnabled;

    // 用于管理定时任务的调度器
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    // 存储正在进行的超时提醒任务
    private final ConcurrentHashMap<Long, ScheduledFuture<?>> timeoutTasks = new ConcurrentHashMap<>();

    @Override
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void checkAndHandleTimeoutApplications() {
        if (!timeoutCheckEnabled) {
            return;
        }

        log.info("开始检查超时的员工申请...");

        try {
            LocalDateTime timeoutThreshold = LocalDateTime.now().minusHours(defaultTimeoutHours);

            // 查找所有待审核且已超时的申请
            List<EmployeeApplication> timeoutApplications = applicationRepository
                .findByStatusInAndSubmittedAtBefore(
                    List.of(ApplicationStatus.PENDING, ApplicationStatus.UNDER_REVIEW),
                    timeoutThreshold
                );

            log.info("发现 {} 个超时申请", timeoutApplications.size());

            for (EmployeeApplication application : timeoutApplications) {
                try {
                    sendTimeoutNotification(application);
                } catch (Exception e) {
                    log.error("发送超时通知失败，申请ID: {}, 错误: {}", application.getId(), e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("检查超时申请时发生错误: {}", e.getMessage(), e);
        }
    }

    @Override
    public void scheduleTimeoutReminder(Long applicationId, int timeoutHours) {
        // 取消现有的任务（如果有的话）
        cancelTimeoutReminder(applicationId);

        // 创建新的超时提醒任务
        ScheduledFuture<?> task = scheduler.schedule(() -> {
            try {
                EmployeeApplication application = applicationRepository.findById(applicationId).orElse(null);
                if (application != null && isApplicationStillPending(application)) {
                    sendTimeoutNotification(application);
                }
            } catch (Exception e) {
                log.error("执行超时提醒任务失败，申请ID: {}, 错误: {}", applicationId, e.getMessage());
            } finally {
                // 任务完成后从集合中移除
                timeoutTasks.remove(applicationId);
            }
        }, timeoutHours, TimeUnit.HOURS);

        timeoutTasks.put(applicationId, task);
        log.info("为申请 {} 设置了 {} 小时后的超时提醒", applicationId, timeoutHours);
    }

    @Override
    public void cancelTimeoutReminder(Long applicationId) {
        ScheduledFuture<?> task = timeoutTasks.remove(applicationId);
        if (task != null && !task.isDone()) {
            task.cancel(false);
            log.info("已取消申请 {} 的超时提醒", applicationId);
        }
    }

    @Override
    public boolean isApplicationTimeout(Long applicationId) {
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId).orElse(null);
            if (application == null || !isApplicationStillPending(application)) {
                return false;
            }

            LocalDateTime timeoutThreshold = LocalDateTime.now().minusHours(defaultTimeoutHours);
            return application.getSubmittedAt() != null &&
                   application.getSubmittedAt().isBefore(timeoutThreshold);
        } catch (Exception e) {
            log.error("检查申请超时状态失败，申请ID: {}, 错误: {}", applicationId, e.getMessage());
            return false;
        }
    }

    @Override
    public int getDefaultTimeoutHours() {
        return defaultTimeoutHours;
    }

    /**
     * 发送超时通知
     */
    private void sendTimeoutNotification(EmployeeApplication application) {
        try {
            notificationService.sendReviewTimeoutReminder(application);
            log.info("已发送超时提醒，申请ID: {}, 申请人: {}",
                application.getId(), application.getName());
        } catch (Exception e) {
            log.error("发送超时通知失败，申请ID: {}, 错误: {}",
                application.getId(), e.getMessage());
            throw e;
        }
    }

    /**
     * 检查申请是否仍处于待审核状态
     */
    private boolean isApplicationStillPending(EmployeeApplication application) {
        return application.getStatus() == ApplicationStatus.PENDING ||
               application.getStatus() == ApplicationStatus.UNDER_REVIEW;
    }

    /**
     * 服务关闭时清理资源
     */
    public void destroy() {
        // 取消所有待执行的任务
        timeoutTasks.values().forEach(task -> task.cancel(false));
        timeoutTasks.clear();

        // 关闭调度器
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }

        log.info("ReviewTimeoutService 已关闭");
    }
}