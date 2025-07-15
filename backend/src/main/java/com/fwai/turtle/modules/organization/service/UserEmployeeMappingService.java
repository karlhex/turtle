package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.UserEmployeeMappingDTO;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.modules.organization.repository.EmployeeRepository;
import com.fwai.turtle.base.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserEmployeeMappingService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<UserEmployeeMappingDTO> getAllMappings() {
        return employeeRepository.findAll().stream()
            .filter(employee -> employee.getUser() != null)
            .map(employee -> {
                UserEmployeeMappingDTO dto = new UserEmployeeMappingDTO();
                dto.setEmployeeId(employee.getId());
                dto.setEmployeeName(employee.getName());
                dto.setUserId(employee.getUser().getId());
                dto.setUsername(employee.getUser().getUsername());
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public void createMapping(Long userId, Long employeeId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        // 检查是否已经存在映射
        if (employee.getUser() != null) {
            throw new RuntimeException("Employee already mapped to a user");
        }
        
        employee.setUser(user);
        employeeRepository.save(employee);
    }

    @Transactional
    public void deleteMapping(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        employee.setUser(null);
        employeeRepository.save(employee);
    }

    @Transactional(readOnly = true)
    public List<User> getUnmappedUsers() {
        return userRepository.findAll().stream()
            .filter(user -> user.getEmployee() == null)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Employee> getUnmappedEmployees() {
        return employeeRepository.findAll().stream()
            .filter(employee -> employee.getUser() == null)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserEmployeeMappingDTO getMappingByUserId(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getEmployee() == null) {
            return null;
        }
        UserEmployeeMappingDTO dto = new UserEmployeeMappingDTO();
        dto.setEmployeeId(user.getEmployee().getId());
        dto.setEmployeeName(user.getEmployee().getName());
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        return dto;
    }
}
