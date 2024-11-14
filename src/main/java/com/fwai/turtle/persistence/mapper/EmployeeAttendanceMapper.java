package com.fwai.turtle.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import com.fwai.turtle.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.persistence.entity.EmployeeAttendance;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeAttendanceMapper {
    EmployeeAttendanceDTO toDTO(EmployeeAttendance attendance);
    EmployeeAttendance toEntity(EmployeeAttendanceDTO attendanceDTO);
} 