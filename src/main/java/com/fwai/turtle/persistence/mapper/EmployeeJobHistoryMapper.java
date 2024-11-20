package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.persistence.entity.EmployeeJobHistory;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeJobHistoryMapper {
    EmployeeJobHistoryDTO toDTO(EmployeeJobHistory jobHistory);
    EmployeeJobHistory toEntity(EmployeeJobHistoryDTO jobHistoryDTO);
} 