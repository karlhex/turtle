package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.RolePermissionDTO;
import com.fwai.turtle.persistence.entity.RolePermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RolePermissionMapper {
    RolePermissionMapper INSTANCE = Mappers.getMapper(RolePermissionMapper.class);

    @Mapping(target = "roleName", source = "role.name")
    @Mapping(target = "permission", source = "transactionPattern")
    RolePermissionDTO toDTO(RolePermission rolePermission);

    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    RolePermission toEntity(RolePermissionDTO dto);
}
