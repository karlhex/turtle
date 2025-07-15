package com.fwai.turtle.modules.organization.repository;

import com.fwai.turtle.modules.organization.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByIsActiveTrue();
}
