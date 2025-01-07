package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.persistence.entity.EmployeeJobHistory;
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