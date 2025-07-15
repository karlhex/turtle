package com.fwai.turtle.modules.organization.mapper;

import com.fwai.turtle.modules.organization.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.modules.organization.entity.EmployeeJobHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeJobHistoryMapper {

    @Mapping(target = "employeeId", source = "employee.id")
    EmployeeJobHistoryDTO toDTO(EmployeeJobHistory jobHistory);

    @Mapping(target = "employee", ignore = true)
    EmployeeJobHistory toEntity(EmployeeJobHistoryDTO jobHistoryDTO);
} 