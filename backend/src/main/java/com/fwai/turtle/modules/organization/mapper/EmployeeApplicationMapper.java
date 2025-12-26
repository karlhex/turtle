package com.fwai.turtle.modules.organization.mapper;

import com.fwai.turtle.modules.organization.dto.EmployeeApplicationDTO;
import com.fwai.turtle.modules.organization.entity.EmployeeApplication;
import org.mapstruct.*;

/**
 * EmployeeApplicationMapper
 * 员工入职申请实体与DTO映射器
 */
@Mapper(componentModel = "spring")
public interface EmployeeApplicationMapper {

    /**
     * 实体转DTO
     */
    @Mapping(source = "applicantUser.id", target = "applicantUserId")
    @Mapping(source = "applicantUser.username", target = "applicantUserName")
    @Mapping(source = "reviewerUser.id", target = "reviewerUserId")
    @Mapping(source = "reviewerUser.username", target = "reviewerUserName")
    EmployeeApplicationDTO toDTO(EmployeeApplication employeeApplication);

    /**
     * DTO转实体（创建时）
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicantUser", ignore = true)
    @Mapping(target = "reviewerUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    EmployeeApplication toEntity(EmployeeApplicationDTO employeeApplicationDTO);

    /**
     * 更新实体（部分更新）
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicantUser", ignore = true)
    @Mapping(target = "reviewerUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "submittedAt", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    void updateEntity(EmployeeApplicationDTO source, @MappingTarget EmployeeApplication target);

    /**
     * 复制实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EmployeeApplication copy(EmployeeApplication source);
}