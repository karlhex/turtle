package com.fwai.turtle.modules.finance.mapper;

import com.fwai.turtle.modules.finance.dto.ReimbursementDTO;
import com.fwai.turtle.modules.finance.entity.Reimbursement;
import org.mapstruct.Mapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.fwai.turtle.modules.organization.mapper.EmployeeMapper;
import com.fwai.turtle.modules.project.mapper.ProjectMapper;

@SuppressWarnings("mapping-with-ignore")
@Mapper(
    componentModel = "spring",
    uses = {
        EmployeeMapper.class,
        ProjectMapper.class,
        ReimbursementItemMapper.class
    }
)
public interface ReimbursementMapper {
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "items", source = "items")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "applicantId", source = "applicantId")
    @Mapping(target = "approverId", source = "approverId")
    @Mapping(target = "projectId", source = "projectId")
    @Mapping(target = "approvalDate", source = "approvalDate")
    ReimbursementDTO toDTO(Reimbursement reimbursement);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "items", source = "items")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "applicantId", source = "applicantId")
    @Mapping(target = "approverId", source = "approverId")
    @Mapping(target = "projectId", source = "projectId")
    @Mapping(target = "approvalDate", source = "approvalDate")
    Reimbursement toEntity(ReimbursementDTO dto);

    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "projectId", expression = "java(dto.getProjectId() == null ? 0L : dto.getProjectId())")
    void updateEntityFromDTO(ReimbursementDTO dto, @MappingTarget Reimbursement reimbursement);
}
