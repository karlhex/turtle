package com.fwai.turtle.modules.workflow.service;

import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.modules.organization.repository.EmployeeRepository;
import com.fwai.turtle.modules.organization.entity.Department;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 动态角色判断服务
 * 实现复杂的角色判断逻辑，如部门经理判断等
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DynamicRoleService {

    private final EmployeeRepository employeeRepository;

    /**
     * 判断用户是否为指定申请人的部门经理
     * 
     * 逻辑：
     * 1. 输入userId和applicantId
     * 2. 查找两个用户的employee信息
     * 3. 获取applicant的department
     * 4. 如果用户部门与申请人不同，返回false
     * 5. 如果department.manager_id = userId对应的employeeId，则为DEPT_MANAGER
     * 
     * @param userId 当前用户ID
     * @param applicantUserId 申请人用户ID
     * @return 是否为部门经理
     */
    public boolean isDepartmentManager(Long userId, Long applicantUserId) {
        log.info("Checking if user {} is department manager for applicant {}", userId, applicantUserId);
        
        try {
            // 1. 查找当前用户的员工信息
            Optional<Employee> currentUserEmployee = employeeRepository.findByUser_Id(userId);
            if (currentUserEmployee.isEmpty()) {
                log.warn("Current user {} has no employee record", userId);
                return false;
            }

            // 2. 查找申请人的员工信息
            Optional<Employee> applicantEmployee = employeeRepository.findByUser_Id(applicantUserId);
            if (applicantEmployee.isEmpty()) {
                log.warn("Applicant user {} has no employee record", applicantUserId);
                return false;
            }

            Employee currentEmp = currentUserEmployee.get();
            Employee applicantEmp = applicantEmployee.get();

            // 3. 检查部门
            Department currentDept = currentEmp.getDepartment();
            Department applicantDept = applicantEmp.getDepartment();

            if (currentDept == null || applicantDept == null) {
                log.warn("Department information missing for user {} or applicant {}", userId, applicantUserId);
                return false;
            }

            // 4. 如果用户部门与申请人不同，返回false
            if (!currentDept.getId().equals(applicantDept.getId())) {
                log.debug("User {} and applicant {} are in different departments", userId, applicantUserId);
                return false;
            }

            // 5. 检查当前用户是否为该部门的经理
            Long managerId = applicantDept.getManagerId();
            boolean isManager = managerId != null && managerId.equals(currentEmp.getId());
            
            log.info("User {} {} department manager for applicant {} (department: {}, manager_id: {})", 
                    userId, isManager ? "is" : "is not", applicantUserId, 
                    applicantDept.getName(), managerId);
            
            return isManager;

        } catch (Exception e) {
            log.error("Error checking department manager role for user {} and applicant {}", 
                     userId, applicantUserId, e);
            return false;
        }
    }

    /**
     * 判断用户是否为指定员工申请的部门经理（用于员工入职申请）
     * 
     * @param userId 当前用户ID
     * @param applicationId 员工申请ID
     * @return 是否为部门经理
     */
    public boolean isDepartmentManagerForEmployeeApplication(Long userId, Long applicationId) {
        log.info("Checking if user {} is department manager for employee application {}", userId, applicationId);
        
        // 这里需要根据员工申请中的部门信息来判断
        // 暂时返回false，等待员工申请实体完善后实现
        // TODO: 实现基于员工申请的部门经理判断
        
        return false;
    }

    /**
     * 获取指定部门的经理用户ID
     * 
     * @param departmentId 部门ID
     * @return 部门经理的用户ID，如果没有则返回null
     */
    public Long getDepartmentManagerUserId(Long departmentId) {
        log.info("Getting department manager user ID for department {}", departmentId);
        
        try {
            List<Employee> employees = employeeRepository.findByDepartment_IdAndUser_IdIsNotNull(departmentId);
            for (Employee emp : employees) {
                Department dept = emp.getDepartment();
                if (dept != null && dept.getManagerId() != null && dept.getManagerId().equals(emp.getId())) {
                    Long userId = emp.getUser() != null ? emp.getUser().getId() : null;
                    if (userId != null) {
                        log.info("Department {} manager user ID: {}", departmentId, userId);
                        return userId;
                    }
                }
            }
            log.warn("No manager found for department {}", departmentId);
            return null;
        } catch (Exception e) {
            log.error("Error getting department manager for department {}", departmentId, e);
            return null;
        }
    }

    /**
     * 判断用户是否具有指定的简单角色
     * 
     * @param userId 用户ID
     * @param roleName 角色名称
     * @return 是否具有该角色
     */
    public boolean hasSimpleRole(Long userId, String roleName) {
        // 这个方法应该通过用户角色服务来实现
        // 暂时返回false，等待角色服务完善
        // TODO: 实现简单角色判断
        log.info("Checking if user {} has simple role {}", userId, roleName);
        return false;
    }

    /**
     * 综合角色判断：检查用户是否具有处理指定业务的权限
     * 
     * @param userId 用户ID
     * @param businessType 业务类型（EMPLOYEE_APPLICATION, REIMBURSEMENT, etc.）
     * @param businessId 业务ID
     * @param requiredRole 需要的角色
     * @return 是否有权限
     */
    public boolean hasBusinessPermission(Long userId, String businessType, Long businessId, String requiredRole) {
        log.info("Checking business permission for user {} on {} {} with role {}", 
                userId, businessType, businessId, requiredRole);

        switch (requiredRole) {
            case "DEPT_MANAGER":
                switch (businessType) {
                    case "EMPLOYEE_APPLICATION":
                        return isDepartmentManagerForEmployeeApplication(userId, businessId);
                    case "REIMBURSEMENT":
                        // TODO: 实现报销的部门经理判断
                        return false;
                    default:
                        return false;
                }
            case "HR_DIRECTOR":
            case "HR_SPECIALIST":
            case "CEO":
            case "ACCOUNTANT":
            case "PURCHASING_MANAGER":
            case "SALES_MANAGER":
                return hasSimpleRole(userId, requiredRole);
            default:
                log.warn("Unknown role: {}", requiredRole);
                return false;
        }
    }
}