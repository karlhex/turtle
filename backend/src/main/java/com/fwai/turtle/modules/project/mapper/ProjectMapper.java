package com.fwai.turtle.modules.project.mapper;

import com.fwai.turtle.modules.project.dto.ProjectDTO;
import com.fwai.turtle.modules.project.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.fwai.turtle.modules.organization.mapper.EmployeeMapper;

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
