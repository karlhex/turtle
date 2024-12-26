package com.fwai.turtle.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.fwai.turtle.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.persistence.entity.EmployeeAttendance;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeAttendanceMapper {
    @Mapping(target = "employeeId", expression = "java(attendance.getEmployee() != null ? attendance.getEmployee().getId() : null)")
    EmployeeAttendanceDTO toDTO(EmployeeAttendance attendance);
    
    @Mapping(target = "employee", expression = "java(attendanceDTO.getEmployeeId() != null ? new com.fwai.turtle.persistence.entity.Employee(attendanceDTO.getEmployeeId()) : null)")
    EmployeeAttendance toEntity(EmployeeAttendanceDTO attendanceDTO);
} 