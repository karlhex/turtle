package com.fwai.turtle.modules.finance.mapper;

import com.fwai.turtle.modules.finance.entity.TaxInfo;
import com.fwai.turtle.modules.finance.dto.TaxInfoDTO;
import org.mapstruct.*;

/**
 * Tax Info Mapper
 * 税务信息对象映射接口
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TaxInfoMapper {
    
    /**
     * Convert entity to DTO
     * 将实体对象转换为DTO
     */
    TaxInfoDTO toDTO(TaxInfo taxInfo);
    
    /**
     * Convert DTO to entity
     * 将DTO转换为实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaxInfo toEntity(TaxInfoDTO dto);
    
    /**
     * Update entity from DTO
     * 使用DTO更新实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(TaxInfoDTO source, @MappingTarget TaxInfo target);
    
    /**
     * Copy tax info entity
     * 复制税务信息实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TaxInfo toTaxInfo(TaxInfo taxInfo);
    
    /**
     * Update tax info entity
     * 更新税务信息实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateTaxInfo(TaxInfo source, @MappingTarget TaxInfo target);
}
