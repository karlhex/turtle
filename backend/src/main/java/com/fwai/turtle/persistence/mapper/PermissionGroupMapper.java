package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.PermissionGroupDTO;
import com.fwai.turtle.persistence.entity.PermissionGroup;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.RolePermission;
import com.fwai.turtle.persistence.repository.RolePermissionRepository;
import com.fwai.turtle.persistence.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PermissionGroupMapper {
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionGroupDTO toDTO(PermissionGroup entity) {
        if (entity == null) {
            return null;
        }

        return PermissionGroupDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .active(entity.isActive())
                .roleIds(entity.getRoles().stream()
                        .map(Role::getId)
                        .collect(Collectors.toSet()))
                .permissionIds(entity.getPermissions().stream()
                        .map(RolePermission::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    public PermissionGroup toEntity(PermissionGroupDTO dto) {
        if (dto == null) {
            return null;
        }

        PermissionGroup entity = new PermissionGroup();
        updateEntity(entity, dto);
        return entity;
    }

    public void updateEntity(PermissionGroup entity, PermissionGroupDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setActive(dto.isActive());

        // Update roles
        if (dto.getRoleIds() != null) {
            Set<Role> roles = dto.getRoleIds().stream()
                    .map(id -> roleRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Role not found: " + id)))
                    .collect(Collectors.toSet());
            entity.setRoles(roles);
        }

        // Update permissions
        if (dto.getPermissionIds() != null) {
            Set<RolePermission> permissions = dto.getPermissionIds().stream()
                    .map(id -> rolePermissionRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + id)))
                    .collect(Collectors.toSet());
            entity.setPermissions(permissions);
        }
    }
}
