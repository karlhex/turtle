package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeEducationDTO;
import com.fwai.turtle.persistence.entity.EmployeeEducation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeEducationMapper {
    @Mapping(target = "employeeId", source = "employee.id")
    EmployeeEducationDTO toDTO(EmployeeEducation education);
 
    @Mapping(target = "employee", ignore = true)
    EmployeeEducation toEntity(EmployeeEducationDTO educationDTO);

    @Mapping(target = "employee", ignore = true)
    void updateEntity(EmployeeEducationDTO educationDTO, @MappingTarget EmployeeEducation education);
} 