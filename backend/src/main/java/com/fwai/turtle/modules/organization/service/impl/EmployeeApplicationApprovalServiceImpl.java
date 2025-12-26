package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.modules.organization.repository.EmployeeApplicationRepository;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * EmployeeApplicationApprovalServiceImpl
 * 员工入职申请审批服务实现 - 供工作流引擎调用
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeApplicationApprovalServiceImpl implements EmployeeApplicationApprovalService {

    private final EmployeeApplicationRepository applicationRepository;

    @Override
    public boolean updateStatus(Long applicationId, String status) {
        log.info("Updating application {} status to: {}", applicationId, status);
        
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
                
            ApplicationStatus newStatus = ApplicationStatus.valueOf(status);
            application.setStatus(newStatus);
            application.setUpdatedAt(LocalDateTime.now());
            
            applicationRepository.save(application);
            
            log.info("Successfully updated application {} status to: {}", applicationId, status);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to update application {} status to {}: {}", applicationId, status, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void recordApprovalComment(Long applicationId, String reviewerId, String comments, String decision) {
        log.info("Recording approval comment for application {}: reviewer={}, decision={}", 
                applicationId, reviewerId, decision);
        
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
                
            // 添加审批意见到现有意见中
            String existingComments = application.getReviewComments();
            String timestamp = LocalDateTime.now().toString();
            String newComment = String.format("[%s] %s: %s (%s)", 
                    timestamp, reviewerId, comments, decision);
            
            if (existingComments != null && !existingComments.isEmpty()) {
                application.setReviewComments(existingComments + "\n" + newComment);
            } else {
                application.setReviewComments(newComment);
            }
            
            application.setUpdatedAt(LocalDateTime.now());
            applicationRepository.save(application);
            
            log.info("Successfully recorded approval comment for application {}", applicationId);
            
        } catch (Exception e) {
            log.error("Failed to record approval comment for application {}: {}", applicationId, e.getMessage(), e);
            throw new RuntimeException("Failed to record approval comment", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeApplication getApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean applicationExists(Long applicationId) {
        return applicationRepository.existsById(applicationId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getApplicantUserId(Long applicationId) {
        EmployeeApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
        return application.getApplicantUserId();
    }

    @Override
    public boolean completeOnboarding(Long applicationId) {
        log.info("Completing onboarding for application {}", applicationId);
        
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
                
            ApplicationStatus newStatus = ApplicationStatus.APPROVED;
            application.setStatus(newStatus);
            application.setUpdatedAt(LocalDateTime.now());
            
            applicationRepository.save(application);
            
            log.info("Successfully completed onboarding for application {}", applicationId);
            return true;
            
        } catch (Exception e) {
            log.error("Failed to complete onboarding for application {}: {}", applicationId, e.getMessage(), e);
            return false;
        }
    }
}