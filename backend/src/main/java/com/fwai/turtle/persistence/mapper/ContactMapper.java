package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.persistence.entity.Contact;
import com.fwai.turtle.dto.ContactDTO;
import org.mapstruct.*;

/**
 * Contact Mapper
 * 联系人信息对象映射接口
 */
@Mapper(componentModel = "spring",
        uses = {
            CompanyIdMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ContactMapper {
    
    /**
     * Convert entity to DTO
     * 将实体对象转换为DTO
     */
    @Mapping(target = "companyId", source = "company.id")
    @Mapping(target = "companyName", source = "company.fullName")
    ContactDTO toDTO(Contact contact);
    
    /**
     * Convert DTO to entity
     * 将DTO转换为实体对象
     */
    @Mapping(target = "company", source = "companyId")
    Contact toEntity(ContactDTO dto);
    
    /**
     * Update entity from DTO
     * 使用DTO更新实体对象
     */
    @Mapping(target = "company", source = "companyId")
    void updateEntityFromDTO(ContactDTO source, @MappingTarget Contact target);
    
    /**
     * Copy contact entity
     * 复制联系人实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Contact copy(Contact contact);
}
