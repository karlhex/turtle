package com.fwai.turtle.modules.workflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.service.UserService;
import com.fwai.turtle.modules.organization.entity.Employee;

import java.util.List;
import java.util.Map;

/**
 * Flowable工作流服务
 * 封装Flowable引擎的核心功能
 * 
 * @author Claude
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FlowableWorkflowService {
    
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;
    private final UserService userService;
    
    /**
     * 启动工作流程
     * 
     * @param processKey 流程定义键
     * @param variables 流程变量
     * @return 流程实例ID
     */
    public String startProcess(String processKey, Map<String, Object> variables) {
        log.info("Starting process: {} with variables: {}", processKey, variables);
        
        ProcessInstance processInstance = runtimeService
            .startProcessInstanceByKey(processKey, variables);
        
        log.info("Process started successfully. ProcessInstanceId: {}", processInstance.getId());
        return processInstance.getId();
    }
    
    /**
     * 启动工作流程（带业务键）
     * 
     * @param processKey 流程定义键
     * @param businessKey 业务键
     * @param variables 流程变量
     * @return 流程实例ID
     */
    public String startProcessWithBusinessKey(String processKey, String businessKey, Map<String, Object> variables) {
        log.info("Starting process: {} with businessKey: {} and variables: {}", processKey, businessKey, variables);
        
        ProcessInstance processInstance = runtimeService
            .startProcessInstanceByKey(processKey, businessKey, variables);
        
        log.info("Process started successfully. ProcessInstanceId: {}, BusinessKey: {}", 
                processInstance.getId(), processInstance.getBusinessKey());
        return processInstance.getId();
    }
    
    /**
     * 完成任务
     * 
     * @param taskId 任务ID
     * @param variables 任务变量
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        log.info("Completing task: {} with variables: {}", taskId, variables);
        taskService.complete(taskId, variables);
        log.info("Task completed successfully: {}", taskId);
    }
    
    /**
     * 获取用户待办任务
     * 
     * @param assignee 任务分配人
     * @return 任务列表
     */
    public List<Task> getPendingTasks(String assignee) {
        log.debug("Getting pending tasks for assignee: {}", assignee);
        return taskService.createTaskQuery()
            .taskAssignee(assignee)
            .active()
            .orderByTaskCreateTime()
            .desc()
            .list();
    }
    
    /**
     * 获取候选用户任务
     * 
     * @param candidateUser 候选用户
     * @return 任务列表
     */
    public List<Task> getCandidateTasks(String candidateUser) {
        log.debug("Getting candidate tasks for user: {}", candidateUser);
        return taskService.createTaskQuery()
            .taskCandidateUser(candidateUser)
            .active()
            .orderByTaskCreateTime()
            .desc()
            .list();
    }
    
    /**
     * 获取候选组任务
     * 
     * @param candidateGroup 候选组
     * @return 任务列表
     */
    public List<Task> getCandidateGroupTasks(String candidateGroup) {
        log.debug("Getting candidate tasks for group: {}", candidateGroup);
        return taskService.createTaskQuery()
            .taskCandidateGroup(candidateGroup)
            .active()
            .orderByTaskCreateTime()
            .desc()
            .list();
    }
    
    /**
     * 获取用户所有相关任务（包括直接分配和候选任务）
     * 
     * @param userId 用户ID
     * @param userGroups 用户所属的组列表
     * @return 任务列表
     */
    public List<Task> getUserTasks(String userId, List<String> userGroups) {
        log.debug("Getting all tasks for user: {} with groups: {}", userId, userGroups);
        
        // 使用OR查询获取所有相关任务：直接分配 + 候选用户 + 候选组
        var query = taskService.createTaskQuery().active();
        
        if (userGroups != null && !userGroups.isEmpty()) {
            // 查询直接分配给用户的任务 OR 用户作为候选人的任务 OR 用户组作为候选组的任务
            query = query.or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .taskCandidateGroupIn(userGroups)
                .endOr();
        } else {
            // 没有组信息时，只查询直接分配和候选用户任务
            query = query.or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .endOr();
        }
        
        return query.orderByTaskCreateTime().desc().list();
    }
    
    /**
     * 获取用户相关任务 - 根据用户角色自动确定候选组
     * 
     * @param userId 用户ID（优先数字ID，也支持用户名）
     * @return 任务列表
     */
    public List<Task> getUserTasksWithRoles(String userId) {
        log.debug("Getting tasks for user ID: {} with auto-detected roles", userId);
        
        // Get user's candidate groups based on their roles
        List<String> userGroups = getUserCandidateGroups(userId);
        log.debug("User {} candidate groups: {}", userId, userGroups);
        
        var query = taskService.createTaskQuery().active();
        
        if (!userGroups.isEmpty()) {
            // Query for tasks assigned to user OR user is candidate OR user's groups are candidate groups
            query = query.or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .taskCandidateGroupIn(userGroups)
                .endOr();
        } else {
            // No groups found, only check direct assignment and candidate user
            query = query.or()
                .taskAssignee(userId)
                .taskCandidateUser(userId)
                .endOr();
        }
        
        List<Task> tasks = query.orderByTaskCreateTime().desc().list();
        log.info("Found {} tasks for user: {} (groups: {})", tasks.size(), userId, userGroups);
        return tasks;
    }
    
    /**
     * 获取用户的候选组列表
     * 基于用户角色和权限确定用户可以处理哪些候选组的任务
     * 
     * @param userId 用户ID（优先数字ID，也支持用户名或email）
     * @return 候选组列表
     */
    private List<String> getUserCandidateGroups(String userId) {
        List<String> groups = new java.util.ArrayList<>();
        
        try {
            // 尝试通过用户名或ID查找用户
            User user = findUser(userId);
            if (user == null) {
                log.warn("User not found: {}", userId);
                return groups;
            }
            
            log.debug("Found user: {} with roles: {}", user.getUsername(), 
                     user.getRoles().stream().map(Role::getName).toArray());
            
            // 基于用户角色映射到工作流候选组
            for (Role role : user.getRoles()) {
                String roleName = role.getName().toUpperCase();
                switch (roleName) {
                    case "DEPARTMENT_MANAGER":
                    case "DEPT_MANAGER":
                    case "MANAGER":
                        groups.add("DEPT_LEADER");
                        break;
                    case "FINANCE_MANAGER":
                    case "FINANCIAL_MANAGER": 
                    case "FINANCE":
                        groups.add("FINANCE_MANAGER");
                        break;
                    case "GENERAL_MANAGER":
                    case "CEO":
                    case "EXECUTIVE":
                        groups.add("GENERAL_MANAGER");
                        break;
                    case "ADMIN":
                        // 管理员可以看到所有类型的任务
                        groups.add("DEPT_LEADER");
                        groups.add("FINANCE_MANAGER");
                        groups.add("GENERAL_MANAGER");
                        break;
                    default:
                        log.debug("Role {} not mapped to any workflow candidate group", roleName);
                }
            }
            
            // 如果用户有员工信息，也可以基于职位判断
            Employee employee = user.getEmployee();
            if (employee != null && employee.getPosition() != null) {
                String positionName = employee.getPosition().getName();
                if (positionName != null) {
                    positionName = positionName.toUpperCase();
                    if (positionName.contains("经理") || positionName.contains("MANAGER")) {
                        if (positionName.contains("财务") || positionName.contains("FINANCE")) {
                            groups.add("FINANCE_MANAGER");
                        } else if (positionName.contains("总经理") || positionName.contains("GENERAL")) {
                            groups.add("GENERAL_MANAGER");
                        } else {
                            groups.add("DEPT_LEADER");
                        }
                    }
                }
            }
            
            // 去重
            groups = groups.stream().distinct().collect(java.util.stream.Collectors.toList());
            
        } catch (Exception e) {
            log.error("Error determining candidate groups for user: {}", userId, e);
        }
        
        log.debug("User {} mapped to candidate groups: {}", userId, groups);
        return groups;
    }
    
    /**
     * 查找用户，优先使用用户ID，然后是用户名和email
     * 
     * @param userIdentifier 用户标识符（优先数字ID）
     * @return 用户实体，如果未找到返回null
     */
    private User findUser(String userIdentifier) {
        try {
            // 首先尝试按ID查找（如果userIdentifier是数字）
            try {
                Long userId = Long.parseLong(userIdentifier);
                var userOpt = userService.findById(userId);
                if (userOpt.isPresent()) {
                    log.debug("Found user by ID {}: {}", userId, userOpt.get().getUsername());
                    return userOpt.get();
                }
            } catch (NumberFormatException e) {
                // userIdentifier不是数字，继续其他查找方式
                log.debug("Input {} is not a numeric ID, trying other lookup methods", userIdentifier);
            }
            
            // 然后尝试按用户名查找
            var userOpt = userService.findByUsername(userIdentifier);
            if (userOpt.isPresent()) {
                log.debug("Found user by username {}: {}", userIdentifier, userOpt.get().getId());
                return userOpt.get();
            }
            
            // 最后尝试按email查找
            userOpt = userService.findByEmail(userIdentifier);
            if (userOpt.isPresent()) {
                log.debug("Found user by email {}: {} (ID: {})", userIdentifier, 
                         userOpt.get().getUsername(), userOpt.get().getId());
                return userOpt.get();
            }
            
        } catch (Exception e) {
            log.error("Error finding user: {}", userIdentifier, e);
        }
        
        log.warn("User not found with identifier: {}", userIdentifier);
        return null;
    }

    /**
     * 获取用户详细信息（用于调试）
     * 
     * @param userId 用户ID（优先数字ID，也支持用户名或email）
     * @return 用户信息Map
     */
    public Map<String, Object> getUserDebugInfo(String userId) {
        Map<String, Object> userInfo = new java.util.HashMap<>();
        
        try {
            User user = findUser(userId);
            if (user == null) {
                userInfo.put("found", false);
                userInfo.put("message", "User not found: " + userId);
                return userInfo;
            }
            
            userInfo.put("found", true);
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("email", user.getEmail());
            
            // 角色信息
            List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(java.util.stream.Collectors.toList());
            userInfo.put("roles", roleNames);
            
            // 员工信息
            Employee employee = user.getEmployee();
            if (employee != null) {
                userInfo.put("employeeId", employee.getId());
                userInfo.put("employeeName", employee.getName());
                if (employee.getPosition() != null) {
                    userInfo.put("position", employee.getPosition().getName());
                }
                if (employee.getDepartment() != null) {
                    userInfo.put("department", employee.getDepartment().getName());
                }
            } else {
                userInfo.put("employee", null);
            }
            
            // 候选组
            List<String> candidateGroups = getUserCandidateGroups(userId);
            userInfo.put("candidateGroups", candidateGroups);
            
        } catch (Exception e) {
            userInfo.put("error", "Error getting user info: " + e.getMessage());
            log.error("Error getting user debug info for: {}", userId, e);
        }
        
        return userInfo;
    }

    /**
     * 获取任务的候选组
     * 
     * @param taskId 任务ID
     * @return 候选组列表
     */
    public List<String> getTaskCandidateGroups(String taskId) {
        log.debug("Getting candidate groups for task: {}", taskId);
        return taskService.getIdentityLinksForTask(taskId).stream()
            .filter(link -> "candidate".equals(link.getType()) && link.getGroupId() != null)
            .map(link -> link.getGroupId())
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * 获取所有活动任务（管理员功能）
     * 
     * @return 任务列表
     */
    public List<Task> getAllActiveTasks() {
        log.debug("Getting all active tasks");
        return taskService.createTaskQuery()
            .active()
            .orderByTaskCreateTime()
            .desc()
            .list();
    }
    
    /**
     * 认领任务
     * 
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void claimTask(String taskId, String userId) {
        log.info("Claiming task: {} by user: {}", taskId, userId);
        taskService.claim(taskId, userId);
        log.info("Task claimed successfully: {} by user: {}", taskId, userId);
    }
    
    /**
     * 释放任务
     * 
     * @param taskId 任务ID
     */
    public void unclaimTask(String taskId) {
        log.info("Unclaiming task: {}", taskId);
        taskService.unclaim(taskId);
        log.info("Task unclaimed successfully: {}", taskId);
    }
    
    /**
     * 获取流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    public ProcessInstance getProcessInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
    }
    
    /**
     * 根据业务键获取流程实例
     * 
     * @param businessKey 业务键
     * @return 流程实例
     */
    public ProcessInstance getProcessInstanceByBusinessKey(String businessKey) {
        return runtimeService.createProcessInstanceQuery()
            .processInstanceBusinessKey(businessKey)
            .singleResult();
    }
    
    /**
     * 获取流程历史实例
     * 
     * @param processInstanceId 流程实例ID
     * @return 历史流程实例
     */
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery()
            .processInstanceId(processInstanceId)
            .singleResult();
    }
    
    /**
     * 删除流程实例
     * 
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        log.info("Deleting process instance: {} with reason: {}", processInstanceId, deleteReason);
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
        log.info("Process instance deleted successfully: {}", processInstanceId);
    }
    
    /**
     * 挂起流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    public void suspendProcessInstance(String processInstanceId) {
        log.info("Suspending process instance: {}", processInstanceId);
        runtimeService.suspendProcessInstanceById(processInstanceId);
        log.info("Process instance suspended successfully: {}", processInstanceId);
    }
    
    /**
     * 激活流程实例
     * 
     * @param processInstanceId 流程实例ID
     */
    public void activateProcessInstance(String processInstanceId) {
        log.info("Activating process instance: {}", processInstanceId);
        runtimeService.activateProcessInstanceById(processInstanceId);
        log.info("Process instance activated successfully: {}", processInstanceId);
    }
    
    /**
     * 获取所有流程定义
     * 
     * @return 流程定义列表
     */
    @Cacheable(value = "processDefinitions", key = "'all'")
    public List<ProcessDefinition> getAllProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery()
            .active()
            .orderByProcessDefinitionName()
            .asc()
            .list();
    }
    
    /**
     * 部署流程定义
     * 
     * @param resourceName 资源名称
     * @param bpmnXml BPMN XML内容
     * @return 部署ID
     */
    @CacheEvict(value = "processDefinitions", allEntries = true)
    public String deployProcess(String resourceName, String bpmnXml) {
        log.info("Deploying process definition: {}", resourceName);
        
        Deployment deployment = repositoryService.createDeployment()
            .name(resourceName)
            .addString(resourceName, bpmnXml)
            .deploy();
            
        log.info("Process deployed successfully. DeploymentId: {}", deployment.getId());
        return deployment.getId();
    }
    
    /**
     * 设置流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @param variables 变量
     */
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        log.info("Setting process variables for processInstance: {}", processInstanceId);
        runtimeService.setVariables(processInstanceId, variables);
    }
    
    /**
     * 获取流程变量
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程变量
     */
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }
}