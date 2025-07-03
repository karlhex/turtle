package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.RolePermissionDTO;
import com.fwai.turtle.persistence.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for the entity {@link RolePermission} and its DTO {@link RolePermissionDTO}.
 */
@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface RolePermissionMapper {

    /**
     * Converts RolePermission entity to DTO.
     * @param rolePermission the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "roleName", source = "role.name")
    RolePermissionDTO toDTO(RolePermission rolePermission);

    /**
     * Converts RolePermission DTO to entity.
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    RolePermission toEntity(RolePermissionDTO dto);
}
