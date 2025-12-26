package com.fwai.turtle.base.service.impl;

import com.fwai.turtle.base.dto.RolePermissionDTO;
import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.entity.RolePermission;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.enums.PermissionType;
import com.fwai.turtle.base.enums.UserType;
import com.fwai.turtle.base.mapper.RolePermissionMapper;
import com.fwai.turtle.base.repository.RolePermissionRepository;
import com.fwai.turtle.base.repository.RoleRepository;
import com.fwai.turtle.base.repository.UserRepository;
import com.fwai.turtle.base.service.RolePermissionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionServiceImpl implements RolePermissionService {

    private static final String SYSTEM_ROLE = "ROLE_SYSTEM";

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RolePermissionMapper rolePermissionMapper;

    @Override
    public boolean hasPermissionByUsername(String username, String transactionPath) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        
        // System users have all permissions
        if (user.getUserType() == UserType.SYSTEM) {
            return true;
        }
        
        return hasPermission(user.getRoles(), transactionPath);
    }

    public boolean hasPermission(Set<Role> roles, String transactionPath) {
        if (roles.stream().anyMatch(role -> SYSTEM_ROLE.equals(role.getName()))) {
            return true;
        }
        
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
                
        List<RolePermission> permissions = rolePermissionRepository.findByRolesAndActive(roleNames);
        
        // Check for *ALL permissions first
        if (permissions.stream()
                .anyMatch(permission -> 
                    permission.getPermissionType() == PermissionType.ALL &&
                    "*ALL".equals(permission.getTransactionPattern())
                )) {
            return true;
        }
        
        // Check specific permissions
        return permissions.stream()
                .filter(permission -> permission.getPermissionType() == PermissionType.SPECIFIC)
                .anyMatch(permission -> 
                    Pattern.compile(permission.getTransactionPattern())
                           .matcher(transactionPath)
                           .matches()
                );
    }

    public Set<String> getPermittedPatterns(Set<Role> roles) {
        if (roles.stream().anyMatch(role -> SYSTEM_ROLE.equals(role.getName()))) {
            return Set.of(".*");
        }
        
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
                
        List<RolePermission> permissions = rolePermissionRepository.findByRolesAndActive(roleNames);
        
        return permissions.stream()
                .map(RolePermission::getTransactionPattern)
                .collect(Collectors.toSet());
    }

    @Override
    public Page<RolePermissionDTO> findAll(Pageable pageable) {
        return rolePermissionRepository.findAll(pageable)
                .map(rolePermissionMapper::toDTO);
    }

    @Override
    public RolePermissionDTO findById(Long id) {
        return rolePermissionRepository.findById(id)
                .map(rolePermissionMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("RolePermission not found with id: " + id));
    }

    @Override
    @Transactional
    public RolePermissionDTO create(RolePermissionDTO rolePermissionDTO) {
        Role role = roleRepository.findByName(rolePermissionDTO.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + rolePermissionDTO.getRoleName()));

        RolePermission rolePermission = rolePermissionMapper.toEntity(rolePermissionDTO);
        rolePermission.setRole(role);

        return rolePermissionMapper.toDTO(rolePermissionRepository.save(rolePermission));
    }

    @Override
    @Transactional
    public RolePermissionDTO update(Long id, RolePermissionDTO rolePermissionDTO) {
        RolePermission existingRolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RolePermission not found with id: " + id));

        Role role = roleRepository.findByName(rolePermissionDTO.getRoleName())
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + rolePermissionDTO.getRoleName()));

        existingRolePermission.setRole(role);
        existingRolePermission.setTransactionPattern(rolePermissionDTO.getTransactionPattern());
        existingRolePermission.setDescription(rolePermissionDTO.getDescription());

        return rolePermissionMapper.toDTO(rolePermissionRepository.save(existingRolePermission));
    }
    
    @Override
    @Transactional
    public void delete(Long id) {
        RolePermission rolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RolePermission not found with id: " + id));
        rolePermission.setIsActive(false);
        rolePermissionRepository.save(rolePermission);
    }

    @Override
    public List<RolePermissionDTO> findByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found: " + roleName));
        return rolePermissionRepository.findByRoleAndIsActiveTrue(role).stream()
                .map(rolePermissionMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RolePermissionDTO toggleActive(Long id) {
        RolePermission rolePermission = rolePermissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RolePermission not found with id: " + id));
        
        // Toggle the active status
        rolePermission.setIsActive(!rolePermission.getIsActive());
        
        return rolePermissionMapper.toDTO(rolePermissionRepository.save(rolePermission));
    }
}
