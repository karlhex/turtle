package com.fwai.turtle.modules.organization.mapper;

import com.fwai.turtle.modules.organization.dto.PositionDTO;
import com.fwai.turtle.modules.organization.entity.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PositionMapper {
    PositionDTO toDTO(Position position);

    Position toEntity(PositionDTO positionDTO);
}
