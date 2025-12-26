package com.fwai.turtle.modules.organization.mapper;

import com.fwai.turtle.modules.organization.dto.ApplicationHistoryDTO;
import com.fwai.turtle.modules.organization.entity.ApplicationHistory;
import org.mapstruct.*;

import java.util.List;

/**
 * ApplicationHistoryMapper
 * 申请操作历史记录映射器
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ApplicationHistoryMapper {

    /**
     * 实体转DTO
     */
    @Mapping(target = "applicationId", source = "application.id")
    @Mapping(target = "operatorId", source = "operator.id")
    @Mapping(target = "operatorName", source = "operator.username")
    @Mapping(target = "actionTypeDescription", ignore = true)
    @Mapping(target = "fromStatusDescription", ignore = true)
    @Mapping(target = "toStatusDescription", ignore = true)
    ApplicationHistoryDTO toDTO(ApplicationHistory entity);

    /**
     * 实体列表转DTO列表
     */
    List<ApplicationHistoryDTO> toDTOList(List<ApplicationHistory> entities);

    /**
     * DTO转实体（用于创建新记录）
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ApplicationHistory toEntity(ApplicationHistoryDTO dto);

    /**
     * 更新实体（用于更新现有记录）
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "application", ignore = true)
    @Mapping(target = "operator", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(ApplicationHistoryDTO dto, @MappingTarget ApplicationHistory entity);

    /**
     * 实体转DTO后处理，填充描述字段
     */
    @AfterMapping
    default void fillDescriptions(@MappingTarget ApplicationHistoryDTO dto) {
        // 这些方法会在DTO中自动计算，无需额外处理
    }
}