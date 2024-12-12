package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.persistence.entity.BankAccount;
import com.fwai.turtle.dto.BankAccountDTO;
import org.mapstruct.*;

/**
 * Bank Account Mapper
 * 银行账户信息对象映射接口
 */
@Mapper(componentModel = "spring",
        uses = {CurrencyMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BankAccountMapper {
    
    /**
     * Convert entity to DTO
     * 将实体对象转换为DTO
     */
    BankAccountDTO toDTO(BankAccount bankAccount);
    
    /**
     * Convert DTO to entity
     * 将DTO转换为实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BankAccount toEntity(BankAccountDTO dto);
    
    /**
     * Update entity from DTO
     * 使用DTO更新实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(BankAccountDTO source, @MappingTarget BankAccount target);
    
    /**
     * Copy bank account entity
     * 复制银行账户实体对象
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BankAccount copy(BankAccount source);
}
