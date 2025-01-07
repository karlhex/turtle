package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.persistence.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
            DepartmentMapper.class, 
            UserMapper.class,
            EmployeeEducationMapper.class, 
            EmployeeJobHistoryMapper.class, 
            EmployeeAttendanceMapper.class, 
            EmployeeEducationMapper.class,
            EmployeeLeaveMapper.class
        })
public interface EmployeeMapper {

    EmployeeDTO toDTO(Employee employee);

    Employee toEntity(EmployeeDTO employeeDTO);

    void updateEntity(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}