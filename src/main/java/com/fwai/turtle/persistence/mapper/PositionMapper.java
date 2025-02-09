package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.PositionDTO;
import com.fwai.turtle.persistence.entity.Position;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PositionMapper {
    PositionDTO toDTO(Position position);

    Position toEntity(PositionDTO positionDTO);
}
