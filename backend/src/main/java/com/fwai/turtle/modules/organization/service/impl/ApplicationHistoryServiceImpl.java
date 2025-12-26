package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.modules.organization.dto.ApplicationHistoryDTO;
import com.fwai.turtle.modules.organization.entity.ApplicationHistory;
import com.fwai.turtle.modules.organization.entity.ApplicationHistory.ActionType;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.modules.organization.mapper.ApplicationHistoryMapper;
import com.fwai.turtle.modules.organization.repository.ApplicationHistoryRepository;
import com.fwai.turtle.modules.organization.repository.EmployeeApplicationRepository;
import com.fwai.turtle.modules.organization.service.ApplicationHistoryService;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * ApplicationHistoryServiceImpl
 * 申请操作历史记录服务实现类
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationHistoryServiceImpl implements ApplicationHistoryService {

    private final ApplicationHistoryRepository historyRepository;
    private final EmployeeApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final ApplicationHistoryMapper historyMapper;

    @Override
    @Transactional
    public ApplicationHistoryDTO recordHistory(Long applicationId, ActionType actionType,
                                             ApplicationStatus fromStatus, ApplicationStatus toStatus,
                                             Long operatorId, String description, String details) {
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Employee application not found: " + applicationId));

            User operator = null;
            if (operatorId != null) {
                operator = userRepository.findById(operatorId).orElse(null);
            }

            ApplicationHistory history = ApplicationHistory.builder()
                .application(application)
                .actionType(actionType)
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .operator(operator)
                .description(description)
                .details(details)
                .build();

            ApplicationHistory savedHistory = historyRepository.save(history);

            log.info("Recorded application history: applicationId={}, actionType={}, operatorId={}",
                applicationId, actionType, operatorId);

            return historyMapper.toDTO(savedHistory);
        } catch (Exception e) {
            log.error("Failed to record application history: applicationId={}, actionType={}, error={}",
                applicationId, actionType, e.getMessage(), e);
            throw new RuntimeException("Failed to record application history", e);
        }
    }

    @Override
    @Transactional
    public ApplicationHistoryDTO recordHistory(Long applicationId, ActionType actionType,
                                             Long operatorId, String description) {
        return recordHistory(applicationId, actionType, null, null, operatorId, description, null);
    }

    @Override
    @Transactional
    public ApplicationHistoryDTO recordSystemHistory(Long applicationId, ActionType actionType, String description) {
        return recordHistory(applicationId, actionType, null, null, null, description, null);
    }

    @Override
    @Transactional
    public ApplicationHistoryDTO recordWorkflowHistory(Long applicationId, ActionType actionType,
                                                     String workflowTaskId, Long operatorId, String description) {
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Employee application not found: " + applicationId));

            User operator = null;
            if (operatorId != null) {
                operator = userRepository.findById(operatorId).orElse(null);
            }

            ApplicationHistory history = ApplicationHistory.builder()
                .application(application)
                .actionType(actionType)
                .operator(operator)
                .description(description)
                .workflowTaskId(workflowTaskId)
                .build();

            ApplicationHistory savedHistory = historyRepository.save(history);
            return historyMapper.toDTO(savedHistory);
        } catch (Exception e) {
            log.error("Failed to record workflow history: applicationId={}, workflowTaskId={}, error={}",
                applicationId, workflowTaskId, e.getMessage(), e);
            throw new RuntimeException("Failed to record workflow history", e);
        }
    }

    @Override
    public List<ApplicationHistoryDTO> getApplicationHistory(Long applicationId) {
        try {
            List<ApplicationHistory> histories = historyRepository.findByApplication_IdOrderByCreatedAtDesc(applicationId);
            return historyMapper.toDTOList(histories);
        } catch (Exception e) {
            log.error("Failed to get application history: applicationId={}, error={}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to get application history", e);
        }
    }

    @Override
    public Page<ApplicationHistoryDTO> getApplicationHistory(Long applicationId, Pageable pageable) {
        try {
            Page<ApplicationHistory> historyPage = historyRepository.findByApplication_IdOrderByCreatedAtDesc(applicationId, pageable);
            return historyPage.map(historyMapper::toDTO);
        } catch (Exception e) {
            log.error("Failed to get application history page: applicationId={}, error={}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to get application history", e);
        }
    }

    @Override
    public List<ApplicationHistoryDTO> getApplicationStatusHistory(Long applicationId) {
        try {
            List<ApplicationHistory> histories = historyRepository.findStatusChangeHistory(applicationId);
            return historyMapper.toDTOList(histories);
        } catch (Exception e) {
            log.error("Failed to get application status history: applicationId={}, error={}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to get application status history", e);
        }
    }

    @Override
    public Page<ApplicationHistoryDTO> getHistoryByOperator(Long operatorId, Pageable pageable) {
        try {
            Page<ApplicationHistory> historyPage = historyRepository.findByOperator_IdOrderByCreatedAtDesc(operatorId, pageable);
            return historyPage.map(historyMapper::toDTO);
        } catch (Exception e) {
            log.error("Failed to get history by operator: operatorId={}, error={}", operatorId, e.getMessage());
            throw new RuntimeException("Failed to get history by operator", e);
        }
    }

    @Override
    public Page<ApplicationHistoryDTO> getHistoryByActionType(ActionType actionType, Pageable pageable) {
        try {
            Page<ApplicationHistory> historyPage = historyRepository.findByActionTypeOrderByCreatedAtDesc(actionType, pageable);
            return historyPage.map(historyMapper::toDTO);
        } catch (Exception e) {
            log.error("Failed to get history by action type: actionType={}, error={}", actionType, e.getMessage());
            throw new RuntimeException("Failed to get history by action type", e);
        }
    }

    @Override
    public Page<ApplicationHistoryDTO> getHistoryByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        try {
            Page<ApplicationHistory> historyPage = historyRepository.findByCreatedAtBetween(startDate, endDate, pageable);
            return historyPage.map(historyMapper::toDTO);
        } catch (Exception e) {
            log.error("Failed to get history by date range: startDate={}, endDate={}, error={}",
                startDate, endDate, e.getMessage());
            throw new RuntimeException("Failed to get history by date range", e);
        }
    }

    @Override
    public List<ApplicationHistoryDTO> getLatestHistory(Long applicationId, int limit) {
        try {
            Pageable pageable = PageRequest.of(0, limit);
            List<ApplicationHistory> histories = historyRepository.findLatestByApplicationId(applicationId, pageable);
            return historyMapper.toDTOList(histories);
        } catch (Exception e) {
            log.error("Failed to get latest history: applicationId={}, limit={}, error={}",
                applicationId, limit, e.getMessage());
            throw new RuntimeException("Failed to get latest history", e);
        }
    }

    @Override
    @Transactional
    public void deleteApplicationHistory(Long applicationId) {
        try {
            historyRepository.deleteByApplication_Id(applicationId);
            log.info("Deleted application history: applicationId={}", applicationId);
        } catch (Exception e) {
            log.error("Failed to delete application history: applicationId={}, error={}", applicationId, e.getMessage());
            throw new RuntimeException("Failed to delete application history", e);
        }
    }

    @Override
    public long countApplicationHistory(Long applicationId) {
        try {
            return historyRepository.countByApplication_Id(applicationId);
        } catch (Exception e) {
            log.error("Failed to count application history: applicationId={}, error={}", applicationId, e.getMessage());
            return 0;
        }
    }

    @Override
    public Map<ActionType, Long> getOperationStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            // 获取所有操作类型的统计数据
            return Arrays.stream(ActionType.values())
                .collect(Collectors.toMap(
                    actionType -> actionType,
                    actionType -> {
                        try {
                            Page<ApplicationHistory> page = historyRepository.findByActionTypeOrderByCreatedAtDesc(
                                actionType, PageRequest.of(0, 1));
                            return page.getTotalElements();
                        } catch (Exception e) {
                            log.warn("Failed to get count for action type {}: {}", actionType, e.getMessage());
                            return 0L;
                        }
                    }
                ));
        } catch (Exception e) {
            log.error("Failed to get operation statistics: startDate={}, endDate={}, error={}",
                startDate, endDate, e.getMessage());
            throw new RuntimeException("Failed to get operation statistics", e);
        }
    }
}