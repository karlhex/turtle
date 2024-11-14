package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeEducationDTO;
import com.fwai.turtle.persistence.entity.EmployeeEducation;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeEducationMapper {
    EmployeeEducationDTO toDTO(EmployeeEducation education);
 
    EmployeeEducation toEntity(EmployeeEducationDTO educationDTO);

    void updateEntity(EmployeeEducationDTO educationDTO, @MappingTarget EmployeeEducation education);
} 