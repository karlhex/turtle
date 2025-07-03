package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeLeaveDTO;
import com.fwai.turtle.persistence.entity.EmployeeLeave;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeLeaveMapper {
    @Mapping(target = "employeeId", source = "employee.id")
    EmployeeLeaveDTO toDTO(EmployeeLeave leave);
 
    @Mapping(target = "employee", ignore = true)
    EmployeeLeave toEntity(EmployeeLeaveDTO leaveDTO);

    @Mapping(target = "employee", ignore = true)
    void updateEntity(EmployeeLeaveDTO leaveDTO, @MappingTarget EmployeeLeave leave);
}  