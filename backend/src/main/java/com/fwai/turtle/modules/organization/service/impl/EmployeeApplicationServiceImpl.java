package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.modules.organization.dto.EmployeeApplicationDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeDTO;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.modules.organization.mapper.EmployeeApplicationMapper;
import com.fwai.turtle.modules.organization.mapper.EmployeeMapper;
import com.fwai.turtle.modules.organization.repository.EmployeeApplicationRepository;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationService;
import com.fwai.turtle.modules.organization.service.EmployeeService;
import com.fwai.turtle.modules.organization.service.EmployeeApplicationNotificationService;
import com.fwai.turtle.modules.organization.service.WebSocketNotificationService;
import com.fwai.turtle.modules.workflow.service.FlowableWorkflowService;
import com.fwai.turtle.modules.workflow.dto.WorkflowTaskDTO;
import com.fwai.turtle.modules.workflow.dto.ApprovalHistoryDTO;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.repository.UserRepository;
import com.fwai.turtle.base.repository.RoleRepository;
import com.fwai.turtle.base.service.UserService;
import com.fwai.turtle.base.types.ApplicationStatus;
import com.fwai.turtle.base.types.EmployeeStatus;
import com.fwai.turtle.base.enums.UserType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.flowable.engine.HistoryService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;

/**
 * EmployeeApplicationServiceImpl
 * 员工入职申请服务实现
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeApplicationServiceImpl implements EmployeeApplicationService {

    private final EmployeeApplicationRepository applicationRepository;
    private final EmployeeApplicationMapper applicationMapper;
    private final EmployeeMapper employeeMapper;
    private final EmployeeService employeeService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final FlowableWorkflowService flowableWorkflowService;
    private final HistoryService historyService;
    private final EmployeeApplicationNotificationService notificationService;
    private final WebSocketNotificationService webSocketNotificationService;

    @Override
    @Transactional
    public EmployeeApplicationDTO createApplication(EmployeeApplicationDTO applicationDTO, Long applicantUserId) {
        log.info("Creating employee application for user: {}", applicantUserId);
        
        // 验证申请人用户存在
        User applicantUser = userRepository.findById(applicantUserId)
            .orElseThrow(() -> new RuntimeException("Applicant user not found: " + applicantUserId));
            
        // 检查身份证号是否已存在
        if (existsByIdNumber(applicationDTO.getIdNumber())) {
            throw new RuntimeException("ID number already exists: " + applicationDTO.getIdNumber());
        }
        
        // 检查邮箱是否有待处理申请
        if (hasPendingApplicationByEmail(applicationDTO.getEmail())) {
            throw new RuntimeException("Email already has pending application: " + applicationDTO.getEmail());
        }
        
        EmployeeApplication application = applicationMapper.toEntity(applicationDTO);
        application.setApplicantUser(applicantUser);
        application.setStatus(ApplicationStatus.PENDING);
        
        EmployeeApplication savedApplication = applicationRepository.save(application);

        // 启动工作流审批流程
        try {
            startApprovalWorkflow(savedApplication);
            log.info("Employee application created with ID: {} and workflow started", savedApplication.getId());
        } catch (Exception e) {
            log.error("Failed to start workflow for application: {}", savedApplication.getId(), e);
            // 可以选择回滚或者记录错误继续
        }

        // 发送申请提交通知
        try {
            notificationService.sendApplicationSubmittedNotification(savedApplication);
        } catch (Exception e) {
            log.error("Failed to send application submitted notification: {}", e.getMessage());
        }

        return applicationMapper.toDTO(savedApplication);
    }

    @Override
    @Transactional
    public EmployeeApplicationDTO updateApplication(Long id, EmployeeApplicationDTO applicationDTO) {
        log.info("Updating employee application: {}", id);
        
        EmployeeApplication existingApplication = applicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee application not found: " + id));
            
        // 只允许更新未审核或需要补充资料的申请
        if (existingApplication.getStatus() != ApplicationStatus.PENDING && 
            existingApplication.getStatus() != ApplicationStatus.UNDER_REVIEW) {
            throw new RuntimeException("Cannot update application in status: " + existingApplication.getStatus());
        }
        
        applicationMapper.updateEntity(applicationDTO, existingApplication);
        
        // 如果是补充资料，更新状态为已提交
        if (existingApplication.getStatus() == ApplicationStatus.UNDER_REVIEW) {
            existingApplication.setStatus(ApplicationStatus.PENDING);
            existingApplication.setSubmittedAt(LocalDateTime.now());
        }
        
        EmployeeApplication updatedApplication = applicationRepository.save(existingApplication);
        
        log.info("Employee application updated: {}", id);
        return applicationMapper.toDTO(updatedApplication);
    }

    @Override
    public Optional<EmployeeApplicationDTO> findById(Long id) {
        return applicationRepository.findById(id)
            .map(applicationMapper::toDTO);
    }

    @Override
    public Page<EmployeeApplicationDTO> findAll(Pageable pageable) {
        return applicationRepository.findAll(pageable)
            .map(applicationMapper::toDTO);
    }

    @Override
    public Page<EmployeeApplicationDTO> findByStatus(ApplicationStatus status, Pageable pageable) {
        return applicationRepository.findByStatusOrderByCreatedAtDesc(status, pageable)
            .map(applicationMapper::toDTO);
    }

    @Override
    public Page<EmployeeApplicationDTO> findPendingApplications(Pageable pageable) {
        return applicationRepository.findPendingApplications(pageable)
            .map(applicationMapper::toDTO);
    }

    @Override
    public List<EmployeeApplicationDTO> findByApplicantUserId(Long applicantUserId) {
        return applicationRepository.findByApplicantUser_IdOrderByCreatedAtDesc(applicantUserId)
            .stream()
            .map(applicationMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeApplicationDTO reviewApplication(Long id, ApplicationStatus newStatus, String reviewComments, Long reviewerUserId) {
        log.info("Reviewing employee application: {} with status: {}", id, newStatus);
        
        EmployeeApplication application = applicationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee application not found: " + id));
            
        User reviewerUser = userRepository.findById(reviewerUserId)
            .orElseThrow(() -> new RuntimeException("Reviewer user not found: " + reviewerUserId));
        
        // 如果有工作流实例，通过工作流处理审批
        if (application.getWorkflowInstanceId() != null) {
            return processWorkflowReview(application, newStatus, reviewComments, reviewerUserId);
        }
        
        // 记录原状态用于通知
        ApplicationStatus oldStatus = application.getStatus();

        // 兼容旧的直接审批方式
        application.setStatus(newStatus);
        application.setReviewComments(reviewComments);
        application.setReviewerUser(reviewerUser);
        application.setReviewedAt(LocalDateTime.now());

        EmployeeApplication reviewedApplication = applicationRepository.save(application);

        // 发送状态变更通知
        try {
            notificationService.sendApplicationStatusNotification(reviewedApplication, oldStatus, newStatus);
            webSocketNotificationService.sendApplicationStatusUpdate(reviewedApplication, oldStatus, newStatus);

            // 特殊状态的额外通知
            if (newStatus == ApplicationStatus.REJECTED && reviewComments != null) {
                notificationService.sendApplicationRejectedNotification(reviewedApplication, reviewComments);
            }
        } catch (Exception e) {
            log.error("Failed to send notification for application status change: {}", e.getMessage());
        }

        log.info("Employee application reviewed: {} with status: {}", id, newStatus);
        return applicationMapper.toDTO(reviewedApplication);
    }

    @Override
    @Transactional
    public EmployeeDTO approveAndConvertToEmployee(Long applicationId, EmployeeDTO employeeInfo, Long reviewerUserId) {
        log.info("Converting application {} to employee record", applicationId);
        
        EmployeeApplication application = applicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Employee application not found: " + applicationId));
            
        // 先标记申请为已批准
        reviewApplication(applicationId, ApplicationStatus.APPROVED, "Application approved and converted to employee", reviewerUserId);
        
        // 创建员工记录，合并申请信息和HR填写的信息
        EmployeeDTO newEmployeeDTO = mergeApplicationToEmployee(application, employeeInfo);
        EmployeeDTO createdEmployee = employeeService.createEmployee(newEmployeeDTO);
        
        // 创建或更新用户账号，建立Employee-User关联
        User employeeUser = createOrUpdateEmployeeUser(application, createdEmployee, employeeInfo.getRoles());
        
        // 更新员工记录关联用户
        updateEmployeeUserAssociation(createdEmployee.getId(), employeeUser.getId());
        
        // 更新申请状态为已转换
        application.setConvertedToEmployee(true);
        application.setConvertedEmployeeId(createdEmployee.getId());
        application.setConvertedAt(LocalDateTime.now());
        applicationRepository.save(application);

        // 发送转换完成通知
        try {
            notificationService.sendConversionCompletedNotification(application, createdEmployee.getId());
            webSocketNotificationService.sendApplicationConversionUpdate(application, createdEmployee.getId());
        } catch (Exception e) {
            log.error("Failed to send conversion completed notification: {}", e.getMessage());
        }

        log.info("Application {} converted to employee {} with user {}", applicationId, createdEmployee.getId(), employeeUser.getId());
        return createdEmployee;
    }

    private EmployeeDTO mergeApplicationToEmployee(EmployeeApplication application, EmployeeDTO employeeInfo) {
        return EmployeeDTO.builder()
            // 从申请中获取的信息
            .name(application.getName())
            .email(application.getEmail())
            .phone(application.getPhone())
            .birthday(application.getBirthday())
            .gender(application.getGender() != null ? application.getGender().name() : null)
            .ethnicity(application.getEthnicity())
            .idType(application.getIdType() != null ? application.getIdType().name() : null)
            .idNumber(application.getIdNumber())
            .emergencyContactName(application.getEmergencyContactName())
            .emergencyContactPhone(application.getEmergencyContactPhone())
            .socialSecurityNumber(application.getSocialSecurityNumber())
            .providentFundNumber(application.getProvidentFundNumber())
            .bankAccount(application.getBankAccount())
            .bankName(application.getBankName())
            .contractType(employeeInfo.getContractType() != null ? employeeInfo.getContractType() : 
                (application.getPreferredContractType() != null ? application.getPreferredContractType().name() : null))
            
            // 从HR信息中获取的信息
            .employeeNumber(employeeInfo.getEmployeeNumber())
            .departmentId(employeeInfo.getDepartmentId())
            .positionId(employeeInfo.getPositionId())
            .hireDate(employeeInfo.getHireDate())
            .contractDuration(employeeInfo.getContractDuration())
            .contractStartDate(employeeInfo.getContractStartDate())
            .remarks(employeeInfo.getRemarks())
            
            // 角色信息
            .roles(employeeInfo.getRoles())
            
            // 默认状态
            .status(EmployeeStatus.ACTIVE)
            .build();
    }

    /**
     * 创建或更新员工用户账号
     */
    private User createOrUpdateEmployeeUser(EmployeeApplication application, EmployeeDTO employee, List<Long> roleIds) {
        User employeeUser;
        
        // 检查申请人是否已有用户账号
        if (application.getApplicantUser() != null) {
            // 使用现有用户账号，更新为员工类型
            employeeUser = application.getApplicantUser();
            log.info("Updating existing user {} to employee type", employeeUser.getId());
        } else {
            // 根据邮箱查找是否存在用户
            Optional<User> existingUser = userRepository.findByEmail(application.getEmail());
            if (existingUser.isPresent()) {
                employeeUser = existingUser.get();
                log.info("Found existing user by email: {}", employeeUser.getId());
            } else {
                // 创建新用户账号
                employeeUser = new User();
                employeeUser.setUsername(generateUsername(employee));
                employeeUser.setEmail(application.getEmail());
                employeeUser.setPasswordExpired(true); // 强制首次登录修改密码
                log.info("Creating new user with username: {}", employeeUser.getUsername());
            }
        }
        
        // 更新用户类型为员工
        employeeUser.setUserType(UserType.EMPLOYEE);
        
        // 设置用户角色
        if (roleIds != null && !roleIds.isEmpty()) {
            List<Role> roles = roleRepository.findAllById(roleIds);
            employeeUser.getRoles().clear();
            employeeUser.getRoles().addAll(roles);
            log.info("Assigned {} roles to user", roles.size());
        }
        
        return userRepository.save(employeeUser);
    }

    /**
     * 更新员工记录关联用户
     */
    private void updateEmployeeUserAssociation(Long employeeId, Long userId) {
        log.info("Associating employee {} with user {}", employeeId, userId);
        employeeService.updateUserAssociation(employeeId, userId);
    }

    /**
     * 生成用户名
     */
    private String generateUsername(EmployeeDTO employee) {
        String baseUsername = employee.getEmployeeNumber() != null ? 
            employee.getEmployeeNumber().toLowerCase() : 
            employee.getEmail().split("@")[0];
            
        // 检查用户名是否已存在，如果存在则添加后缀
        String username = baseUsername;
        int suffix = 1;
        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + suffix;
            suffix++;
        }
        
        return username;
    }

    @Override
    @Transactional
    public void deleteApplication(Long id) {
        log.info("Deleting employee application: {}", id);
        
        if (!applicationRepository.existsById(id)) {
            throw new RuntimeException("Employee application not found: " + id);
        }
        
        applicationRepository.deleteById(id);
        log.info("Employee application deleted: {}", id);
    }

    @Override
    public boolean existsByIdNumber(String idNumber) {
        return applicationRepository.existsByIdNumber(idNumber);
    }

    @Override
    public boolean hasPendingApplicationByEmail(String email) {
        List<ApplicationStatus> pendingStatuses = Arrays.asList(
            ApplicationStatus.PENDING, 
            ApplicationStatus.UNDER_REVIEW,
            ApplicationStatus.VALIDATED
        );
        return applicationRepository.existsByEmailAndStatusIn(email, pendingStatuses);
    }

    @Override
    public long countByStatus(ApplicationStatus status) {
        return applicationRepository.countByStatus(status);
    }

    @Override
    public ApplicationStatisticsDTO getApplicationStatistics() {
        ApplicationStatisticsDTO stats = new ApplicationStatisticsDTO();
        
        stats.setTotalApplications(applicationRepository.count());
        stats.setPendingCount(countByStatus(ApplicationStatus.PENDING));
        stats.setUnderReviewCount(countByStatus(ApplicationStatus.UNDER_REVIEW));
        stats.setApprovedCount(countByStatus(ApplicationStatus.APPROVED));
        stats.setRejectedCount(countByStatus(ApplicationStatus.REJECTED));
        stats.setValidatedCount(countByStatus(ApplicationStatus.VALIDATED));
        
        return stats;
    }

    /**
     * 启动审批工作流
     * 
     * @param application 员工申请
     */
    private void startApprovalWorkflow(EmployeeApplication application) {
        log.info("Starting approval workflow for application: {}", application.getId());
        
        // 准备工作流变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicationId", application.getId());
        variables.put("applicantUserId", application.getApplicantUserId().toString());
        variables.put("applicantName", application.getName());
        variables.put("expectedSalary", application.getExpectedSalary() != null ? application.getExpectedSalary() : 0);
        variables.put("desiredPosition", application.getDesiredPosition());
        variables.put("email", application.getEmail());
        
        // 使用业务键启动工作流
        String businessKey = "employee-application-" + application.getId();
        String processInstanceId = flowableWorkflowService.startProcessWithBusinessKey(
            "employee-application-approval", 
            businessKey, 
            variables
        );
        
        // 可以选择将processInstanceId存储到申请记录中
        application.setWorkflowInstanceId(processInstanceId);
        applicationRepository.save(application);
        
        log.info("Workflow started for application: {} with processInstanceId: {}", 
                application.getId(), processInstanceId);
    }

    /**
     * 通过工作流处理审批
     * 
     * @param applicationId 申请ID
     * @param taskId 任务ID
     * @param decision 审批决定
     * @param comments 审批意见
     * @param reviewerUserId 审批人ID
     */
    private void processWorkflowApproval(Long applicationId, String taskId, 
                                       ApplicationStatus decision, String comments, Long reviewerUserId) {
        log.info("Processing workflow approval for application: {}, task: {}, decision: {}", 
                applicationId, taskId, decision);
        
        // 准备任务变量
        Map<String, Object> variables = new HashMap<>();
        
        // 根据当前任务设置相应的决定变量
        if (taskId.contains("hrInitialReview")) {
            variables.put("hrApprovalDecision", decision.name());
            variables.put("hrReviewComments", comments);
            variables.put("hrReviewerId", reviewerUserId.toString());
        } else if (taskId.contains("deptManagerApproval")) {
            variables.put("deptApprovalDecision", decision.name());
            variables.put("deptReviewComments", comments);
            variables.put("deptReviewerId", reviewerUserId.toString());
        } else if (taskId.contains("generalManagerApproval")) {
            variables.put("gmApprovalDecision", decision.name());
            variables.put("gmReviewComments", comments);
            variables.put("gmReviewerId", reviewerUserId.toString());
        }
        
        // 完成工作流任务
        flowableWorkflowService.completeTask(taskId, variables);
        
        log.info("Workflow task completed: {} for application: {}", taskId, applicationId);
    }

    /**
     * 通过工作流处理审批
     * 
     * @param application 申请实体
     * @param newStatus 新状态
     * @param reviewComments 审批意见
     * @param reviewerUserId 审批人ID
     * @return 审批后的申请DTO
     */
    private EmployeeApplicationDTO processWorkflowReview(EmployeeApplication application, 
                                                       ApplicationStatus newStatus, 
                                                       String reviewComments, 
                                                       Long reviewerUserId) {
        log.info("Processing workflow review for application: {} with status: {}", 
                application.getId(), newStatus);
        
        try {
            // 获取当前用户的工作流任务
            List<org.flowable.task.api.Task> tasks = flowableWorkflowService
                .getUserTasksWithRoles(reviewerUserId.toString());
            
            // 找到与当前申请相关的任务
            org.flowable.task.api.Task currentTask = null;
            for (org.flowable.task.api.Task task : tasks) {
                // 检查任务是否属于当前申请的工作流实例
                if (application.getWorkflowInstanceId().equals(task.getProcessInstanceId())) {
                    currentTask = task;
                    break;
                }
            }
            
            if (currentTask == null) {
                log.warn("No workflow task found for application: {} and reviewer: {}", 
                        application.getId(), reviewerUserId);
                // 回退到直接审批方式
                return performDirectReview(application, newStatus, reviewComments, reviewerUserId);
            }
            
            // 通过工作流处理审批
            processWorkflowApproval(application.getId(), currentTask.getId(), 
                                  newStatus, reviewComments, reviewerUserId);
            
            // 刷新申请状态（工作流应该已经更新了状态）
            application = applicationRepository.findById(application.getId())
                .orElseThrow(() -> new RuntimeException("Application not found after workflow processing"));
            
            log.info("Workflow review completed for application: {}", application.getId());
            return applicationMapper.toDTO(application);
            
        } catch (Exception e) {
            log.error("Error processing workflow review for application: {}", application.getId(), e);
            // 出错时回退到直接审批方式
            return performDirectReview(application, newStatus, reviewComments, reviewerUserId);
        }
    }

    /**
     * 直接审批方式（兼容旧的审批流程）
     * 
     * @param application 申请实体
     * @param newStatus 新状态
     * @param reviewComments 审批意见
     * @param reviewerUserId 审批人ID
     * @return 审批后的申请DTO
     */
    private EmployeeApplicationDTO performDirectReview(EmployeeApplication application,
                                                     ApplicationStatus newStatus,
                                                     String reviewComments,
                                                     Long reviewerUserId) {
        log.info("Performing direct review for application: {} with status: {}", 
                application.getId(), newStatus);
        
        User reviewerUser = userRepository.findById(reviewerUserId)
            .orElseThrow(() -> new RuntimeException("Reviewer user not found: " + reviewerUserId));
        
        application.setStatus(newStatus);
        application.setReviewComments(reviewComments);
        application.setReviewerUser(reviewerUser);
        application.setReviewedAt(LocalDateTime.now());
        
        EmployeeApplication reviewedApplication = applicationRepository.save(application);
        
        log.info("Direct review completed for application: {} with status: {}", 
                application.getId(), newStatus);
        return applicationMapper.toDTO(reviewedApplication);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkflowTaskDTO> getUserPendingTasks(Long userId) {
        log.debug("Getting pending workflow tasks for user: {}", userId);
        
        try {
            List<Task> tasks = flowableWorkflowService.getUserTasksWithRoles(userId.toString());
            
            return tasks.stream()
                .filter(task -> "employee-application-approval".equals(task.getProcessDefinitionId()))
                .map(this::convertToWorkflowTaskDTO)
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error getting pending tasks for user: {}", userId, e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovalHistoryDTO> getApprovalHistory(Long applicationId) {
        log.debug("Getting approval history for application: {}", applicationId);
        
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
                
            if (application.getWorkflowInstanceId() == null) {
                log.debug("No workflow instance found for application: {}", applicationId);
                return java.util.Collections.emptyList();
            }
            
            List<HistoricTaskInstance> historicTasks = historyService
                .createHistoricTaskInstanceQuery()
                .processInstanceId(application.getWorkflowInstanceId())
                .orderByTaskCreateTime()
                .asc()
                .list();
                
            return historicTasks.stream()
                .map(task -> convertToApprovalHistoryDTO(task, applicationId))
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            log.error("Error getting approval history for application: {}", applicationId, e);
            return java.util.Collections.emptyList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public WorkflowTaskDTO getCurrentApprovalTask(Long applicationId) {
        log.debug("Getting current approval task for application: {}", applicationId);
        
        try {
            EmployeeApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found: " + applicationId));
                
            if (application.getWorkflowInstanceId() == null) {
                log.debug("No workflow instance found for application: {}", applicationId);
                return null;
            }
            
            List<Task> tasks = flowableWorkflowService.getAllActiveTasks().stream()
                .filter(task -> task.getProcessInstanceId().equals(application.getWorkflowInstanceId()))
                .collect(Collectors.toList());
                
            if (tasks.isEmpty()) {
                return null;
            }
            
            return convertToWorkflowTaskDTO(tasks.get(0));
            
        } catch (Exception e) {
            log.error("Error getting current task for application: {}", applicationId, e);
            return null;
        }
    }
    
    /**
     * 转换Task为WorkflowTaskDTO
     */
    private WorkflowTaskDTO convertToWorkflowTaskDTO(Task task) {
        Map<String, Object> processVariables = flowableWorkflowService
            .getProcessVariables(task.getProcessInstanceId());
            
        return WorkflowTaskDTO.builder()
            .id(task.getId())
            .name(task.getName())
            .description(task.getDescription())
            .assignee(task.getAssignee())
            .processInstanceId(task.getProcessInstanceId())
            .processDefinitionKey(task.getProcessDefinitionId())
            .createTime(task.getCreateTime() != null ? 
                LocalDateTime.ofInstant(task.getCreateTime().toInstant(), ZoneId.systemDefault()) : null)
            .dueDate(task.getDueDate() != null ? 
                LocalDateTime.ofInstant(task.getDueDate().toInstant(), ZoneId.systemDefault()) : null)
            .priority(task.getPriority())
            .category(task.getCategory())
            .formKey(task.getFormKey())
            .executionId(task.getExecutionId())
            .processVariables(processVariables)
            .candidateGroups(flowableWorkflowService.getTaskCandidateGroups(task.getId()))
            .applicationId(processVariables.get("applicationId") != null ? 
                Long.valueOf(processVariables.get("applicationId").toString()) : null)
            .applicantName((String) processVariables.get("applicantName"))
            .applicantEmail((String) processVariables.get("email"))
            .build();
    }
    
    /**
     * 转换HistoricTaskInstance为ApprovalHistoryDTO
     */
    private ApprovalHistoryDTO convertToApprovalHistoryDTO(HistoricTaskInstance task, Long applicationId) {
        return ApprovalHistoryDTO.builder()
            .taskId(task.getId())
            .taskName(task.getName())
            .assignee(task.getAssignee())
            .assigneeName(task.getAssignee()) // 可以进一步查询用户名
            .startTime(task.getCreateTime() != null ? 
                LocalDateTime.ofInstant(task.getCreateTime().toInstant(), ZoneId.systemDefault()) : null)
            .endTime(task.getEndTime() != null ? 
                LocalDateTime.ofInstant(task.getEndTime().toInstant(), ZoneId.systemDefault()) : null)
            .durationInMillis(task.getDurationInMillis())
            .deleteReason(task.getDeleteReason())
            .processInstanceId(task.getProcessInstanceId())
            .processDefinitionKey(task.getProcessDefinitionId())
            .activityId(task.getTaskDefinitionKey())
            .activityName(task.getName())
            .applicationId(applicationId)
            .stepType(mapTaskToStepType(task.getTaskDefinitionKey()))
            .build();
    }
    
    /**
     * 映射任务定义键到步骤类型
     */
    private String mapTaskToStepType(String taskDefinitionKey) {
        if (taskDefinitionKey == null) {
            return "UNKNOWN";
        }
        
        switch (taskDefinitionKey) {
            case "hrInitialReview":
                return "HR_REVIEW";
            case "deptManagerApproval":
                return "DEPT_APPROVAL";
            case "generalManagerApproval":
                return "GM_APPROVAL";
            case "waitForSupplementary":
                return "SUPPLEMENTARY_WAIT";
            default:
                return taskDefinitionKey.toUpperCase();
        }
    }
}