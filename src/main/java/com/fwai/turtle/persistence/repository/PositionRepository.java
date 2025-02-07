package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    List<Position> findByDepartmentId(Long departmentId);
    List<Position> findByIsActiveTrue();
}
