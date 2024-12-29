package com.fwai.turtle.persistence.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.fwai.turtle.dto.RoleDTO;
import com.fwai.turtle.persistence.entity.Role;

@Component
public class RoleMapper {
    
    public RoleDTO toDTO(Role role) {
        if (role == null) {
            return null;
        }

        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .isSystem(role.getIsSystem())
                .build();
    }

    public List<RoleDTO> toDTOList(List<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Role toEntity(RoleDTO dto) {
        if (dto == null) {
            return null;
        }

        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setIsSystem(dto.getIsSystem());
        return role;
    }
}
