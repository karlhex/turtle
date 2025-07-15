package com.fwai.turtle.modules.customer.mapper;

import com.fwai.turtle.modules.customer.entity.Company;
import com.fwai.turtle.modules.customer.dto.CompanyDTO;
import org.mapstruct.*;
import com.fwai.turtle.modules.finance.mapper.BankAccountMapper;
import com.fwai.turtle.modules.finance.mapper.TaxInfoMapper;
import com.fwai.turtle.modules.customer.mapper.ContactMapper;

/**
 * Company Mapper
 * 公司信息对象映射接口
 */
@Mapper(componentModel = "spring",
        uses = {
            BankAccountMapper.class,
            TaxInfoMapper.class,
            ContactMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompanyMapper {
    
    /**
     * Convert entity to DTO
     * 将实体对象转换为DTO
     */
    CompanyDTO toDTO(Company company);

    /**
     * Convert DTO to entity
     * 将DTO转换为实体对象
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Company toEntity(CompanyDTO dto);
    
    /**
     * Update entity from DTO
     * 使用DTO更新实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(CompanyDTO source, @MappingTarget Company target);
    
    /**
     * Copy company entity
     * 复制公司实体对象
     */
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Company toCompany(Company company);
    
    /**
     * Update company entity
     * 更新公司实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateCompany(Company source, @MappingTarget Company target);

}
