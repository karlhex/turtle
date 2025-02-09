package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.persistence.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {
            DepartmentMapper.class, 
            UserMapper.class,
            EmployeeEducationMapper.class, 
            EmployeeJobHistoryMapper.class, 
            EmployeeAttendanceMapper.class,
            DepartmentIdMapper.class,
            PositionIdMapper.class,
            EmployeeLeaveMapper.class
        })
public interface EmployeeMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "positionId", source = "position.id")
    EmployeeDTO toDTO(Employee employee);

    @Mapping(target = "department", source = "departmentId")
    @Mapping(target = "position", source = "positionId")
    Employee toEntity(EmployeeDTO employeeDTO);

    @Mapping(target = "department", source = "departmentId")
    @Mapping(target = "position", source = "positionId")
    void updateEntity(EmployeeDTO employeeDTO, @MappingTarget Employee employee);
}