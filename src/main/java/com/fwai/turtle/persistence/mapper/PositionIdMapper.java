package com.fwai.turtle.persistence.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.persistence.entity.Position;
import com.fwai.turtle.persistence.repository.PositionRepository;

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
