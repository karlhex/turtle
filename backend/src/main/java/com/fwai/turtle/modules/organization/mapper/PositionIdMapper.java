package com.fwai.turtle.modules.organization.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.modules.organization.entity.Position;
import com.fwai.turtle.modules.organization.repository.PositionRepository;

@Component
public class PositionIdMapper {

    private final PositionRepository positionRepository;

    public PositionIdMapper(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Position fromId(Long id) {
        if (id == null) return null;
        Position position = positionRepository.findById(id).orElse(null);

        return position;
    }
}
