package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeLeaveDTO;
import com.fwai.turtle.persistence.entity.EmployeeLeave;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeLeaveMapper {
    EmployeeLeaveDTO toDTO(EmployeeLeave leave);
 
    EmployeeLeave toEntity(EmployeeLeaveDTO leaveDTO);

    void updateEntity(EmployeeLeaveDTO leaveDTO, @MappingTarget EmployeeLeave leave);
}  