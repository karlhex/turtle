package com.fwai.turtle.persistence.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.fwai.turtle.dto.EmployeePayrollDTO;
import com.fwai.turtle.persistence.entity.EmployeePayroll;

@Mapper(componentModel = "spring")
public interface EmployeePayrollMapper {
    /**
     * Convert DTO to Entity
     * @param dto the DTO to convert
     * @return the converted entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "bankAccount", ignore = true)
    EmployeePayroll toEntity(EmployeePayrollDTO dto);

    /**
     * Convert Entity to DTO
     * @param entity the entity to convert
     * @return the converted DTO
     */
    @Mapping(target = "bankAccountId", source = "bankAccount.id")
    EmployeePayrollDTO toDTO(EmployeePayroll entity);

    /**
     * Update Entity from DTO
     * @param dto the source DTO
     * @param entity the target entity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "bankAccount", ignore = true)
    void updateEntity(EmployeePayrollDTO dto, @MappingTarget EmployeePayroll entity);
}
