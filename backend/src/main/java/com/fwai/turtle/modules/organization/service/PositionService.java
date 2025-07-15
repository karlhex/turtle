package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.PositionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface PositionService {
    PositionDTO create(PositionDTO positionDTO);
    PositionDTO update(Long id, PositionDTO positionDTO);
    void delete(Long id);
    PositionDTO findById(Long id);
    Page<PositionDTO> findAll(Pageable pageable);
    List<PositionDTO> findActive();
}
