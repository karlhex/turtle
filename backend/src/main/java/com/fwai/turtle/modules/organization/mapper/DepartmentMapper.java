package com.fwai.turtle.modules.organization.mapper;

import com.fwai.turtle.modules.organization.dto.DepartmentDTO;
import com.fwai.turtle.modules.organization.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    
    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    DepartmentDTO toDTO(Department department);

    Department toEntity(DepartmentDTO departmentDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(DepartmentDTO dto, @MappingTarget Department entity);
}
