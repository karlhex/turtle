package com.fwai.turtle.base.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fwai.turtle.security.dto.UserDTO;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.repository.RoleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserMapper {
    
    private final RoleRepository roleRepository;
    
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        Employee employee = user.getEmployee();
        String employeeName = employee == null ? null : employee.getName();
        Long employeeId = employee == null ? null : employee.getId();
        
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .userType(user.getUserType())
                .roleNames(user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()))
                .employeeName(employeeName)
                .employeeId(employeeId)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public List<UserDTO> toDTOList(List<User> users) {
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setUserType(userDTO.getUserType());
        
        log.info("UserMapper.toEntity - DTO userType: {}, setting to User: {}", userDTO.getUserType(), userDTO.getUserType());
        
        // Convert role names to Role entities, adding ROLE_ prefix if not present
        if (userDTO.getRoleNames() != null) {
            Set<Role> roles = userDTO.getRoleNames().stream()
                .map(name -> {
                    String roleName = name;
                    return roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                })
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        
        return user;
    }
}
