package com.fwai.turtle.persistence.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fwai.turtle.dto.UserDTO;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.persistence.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

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
                .roleNames(user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
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
        
        // Convert role names to Role entities, adding ROLE_ prefix if not present
        if (userDTO.getRoleNames() != null) {
            Set<Role> roles = userDTO.getRoleNames().stream()
                .map(name -> {
                    String roleName = name.startsWith("ROLE_") ? name : "ROLE_" + name;
                    return roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                })
                .collect(Collectors.toSet());
            user.setRoles(roles);
        }
        
        return user;
    }
}
