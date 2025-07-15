package com.fwai.turtle.base.service.impl;

import com.fwai.turtle.base.dto.PermissionGroupDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.base.entity.PermissionGroup;
import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.entity.RolePermission;
import com.fwai.turtle.base.mapper.PermissionGroupMapper;
import com.fwai.turtle.base.repository.PermissionGroupRepository;
import com.fwai.turtle.base.repository.RolePermissionRepository;
import com.fwai.turtle.base.repository.RoleRepository;
import com.fwai.turtle.base.service.PermissionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionGroupServiceImpl implements PermissionGroupService {
    private final PermissionGroupRepository permissionGroupRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionGroupMapper permissionGroupMapper;

    @Override
    @Transactional
    public PermissionGroupDTO create(PermissionGroupDTO dto) {
        if (permissionGroupRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Permission group with name " + dto.getName() + " already exists");
        }
        
        PermissionGroup entity = permissionGroupMapper.toEntity(dto);
        return permissionGroupMapper.toDTO(permissionGroupRepository.save(entity));
    }

    @Override
    @Transactional
    public PermissionGroupDTO update(Long id, PermissionGroupDTO dto) {
        PermissionGroup entity = permissionGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + id));
        
        if (!entity.getName().equals(dto.getName()) && permissionGroupRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Permission group with name " + dto.getName() + " already exists");
        }
        
        permissionGroupMapper.updateEntity(entity, dto);
        return permissionGroupMapper.toDTO(permissionGroupRepository.save(entity));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!permissionGroupRepository.existsById(id)) {
            throw new ResourceNotFoundException("Permission group not found: " + id);
        }
        permissionGroupRepository.deleteById(id);
    }

    @Override
    public PermissionGroupDTO findById(Long id) {
        return permissionGroupRepository.findById(id)
                .map(permissionGroupMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + id));
    }

    @Override
    public Page<PermissionGroupDTO> findAll(Pageable pageable) {
        return permissionGroupRepository.findAll(pageable)
                .map(permissionGroupMapper::toDTO);
    }

    @Override
    public Page<PermissionGroupDTO> findAllActive(Pageable pageable) {
        return permissionGroupRepository.findAllActive(pageable)
                .map(permissionGroupMapper::toDTO);
    }

    @Override
    public Page<PermissionGroupDTO> search(String query, Pageable pageable) {
        return permissionGroupRepository.search(query, pageable)
                .map(permissionGroupMapper::toDTO);
    }

    @Override
    public Set<PermissionGroupDTO> findByRoleIds(Set<Long> roleIds) {
        return permissionGroupRepository.findByRoleIds(roleIds).stream()
                .map(permissionGroupMapper::toDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void toggleActive(Long id) {
        PermissionGroup group = permissionGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + id));
        group.setActive(!group.isActive());
        permissionGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void addRolesToGroup(Long groupId, Set<Long> roleIds) {
        PermissionGroup group = permissionGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + groupId));
        
        Set<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleId)))
                .collect(Collectors.toSet());
        
        group.getRoles().addAll(roles);
        permissionGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void removeRolesFromGroup(Long groupId, Set<Long> roleIds) {
        PermissionGroup group = permissionGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + groupId));
        
        group.getRoles().removeIf(role -> roleIds.contains(role.getId()));
        permissionGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void addPermissionsToGroup(Long groupId, Set<Long> permissionIds) {
        PermissionGroup group = permissionGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + groupId));
        
        Set<RolePermission> permissions = permissionIds.stream()
                .map(permId -> rolePermissionRepository.findById(permId)
                        .orElseThrow(() -> new ResourceNotFoundException("Permission not found: " + permId)))
                .collect(Collectors.toSet());
        
        group.getPermissions().addAll(permissions);
        permissionGroupRepository.save(group);
    }

    @Override
    @Transactional
    public void removePermissionsFromGroup(Long groupId, Set<Long> permissionIds) {
        PermissionGroup group = permissionGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission group not found: " + groupId));
        
        group.getPermissions().removeIf(permission -> permissionIds.contains(permission.getId()));
        permissionGroupRepository.save(group);
    }
}
