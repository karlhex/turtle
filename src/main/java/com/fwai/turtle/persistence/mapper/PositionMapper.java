package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.PositionDTO;
import com.fwai.turtle.persistence.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PositionMapper {
    PositionMapper INSTANCE = Mappers.getMapper(PositionMapper.class);

    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "departmentId", source = "department.id")
    PositionDTO toDTO(Position position);

    @Mapping(target = "department", ignore = true)
    Position toEntity(PositionDTO positionDTO);
}
