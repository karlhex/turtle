package com.fwai.turtle.modules.finance.mapper;

import com.fwai.turtle.modules.finance.dto.ReimbursementItemDTO;
import com.fwai.turtle.modules.finance.entity.ReimbursementItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReimbursementItemMapper {
    @Mapping(source = "reimbursement.id", target = "reimbursementId")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ReimbursementItemDTO toDTO(ReimbursementItem item);

    @Mapping(target = "reimbursement", ignore = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    ReimbursementItem toEntity(ReimbursementItemDTO dto);

    @Mapping(target = "reimbursement", ignore = true)
    @Mapping(source = "id", target = "id")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    void updateEntityFromDTO(ReimbursementItemDTO dto, @MappingTarget ReimbursementItem item);
}
