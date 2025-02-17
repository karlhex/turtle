package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.ProjectDTO;
import com.fwai.turtle.persistence.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ContractMapper.class, EmployeeMapper.class})
public interface ProjectMapper {
    
    @Mapping(target = "manager", source = "manager")
    ProjectDTO toDTO(Project project);

    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "contracts", ignore = true)
    Project toEntity(ProjectDTO projectDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "contracts", ignore = true)
    void updateEntity(ProjectDTO projectDTO, @MappingTarget Project project);
}
