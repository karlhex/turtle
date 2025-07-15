package com.fwai.turtle.modules.organization.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.fwai.turtle.modules.organization.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.modules.organization.entity.EmployeeAttendance;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeAttendanceMapper {
    @Mapping(target = "employeeId", expression = "java(attendance.getEmployee() != null ? attendance.getEmployee().getId() : null)")
    EmployeeAttendanceDTO toDTO(EmployeeAttendance attendance);
    
    @Mapping(target = "employee", expression = "java(attendanceDTO.employeeId() != null ? new com.fwai.turtle.modules.organization.entity.Employee(attendanceDTO.employeeId()) : null)")
    EmployeeAttendance toEntity(EmployeeAttendanceDTO attendanceDTO);
} 