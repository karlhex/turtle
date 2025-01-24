package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.RolePermissionDTO;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.RolePermission;
import com.fwai.turtle.persistence.mapper.RolePermissionMapper;
import com.fwai.turtle.persistence.repository.RolePermissionRepository;
import com.fwai.turtle.persistence.repository.RoleRepository;
import com.fwai.turtle.service.interfaces.RolePermissionService;

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
    private final RolePermissionMapper rolePermissionMapper;

    public boolean hasPermission(Set<Role> roles, String transactionPath) {
        if (roles.stream().anyMatch(role -> SYSTEM_ROLE.equals(role.getName()))) {
            return true;
        }
        
        Set<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
                
        List<RolePermission> permissions = rolePermissionRepository.findByRolesAndActive(roleNames);
        
        return permissions.stream()
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
