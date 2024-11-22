package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.Department;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", 
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {PersonMapper.class})
public interface EmployeeMapper {

    @Mapping(target = "department", source = "department", qualifiedByName = "toDepartmentDTO")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", source = "department", qualifiedByName = "toDepartmentEntity")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", source = "department", qualifiedByName = "toDepartmentEntity")
    void updateEntity(EmployeeDTO employeeDTO, @MappingTarget Employee employee);

    @Named("toDepartmentDTO")
    default Department toDepartmentDTO(Department department) {
        if (department == null) {
            return null;
        }
        Department dto = new Department();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    @Named("toDepartmentEntity")
    default Department toDepartmentEntity(Department department) {
        if (department == null) {
            return null;
        }
        Department entity = new Department();
        entity.setId(department.getId());
        entity.setName(department.getName());
        return entity;
    }
}