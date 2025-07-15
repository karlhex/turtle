package com.fwai.turtle.modules.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.modules.organization.entity.EmployeeLeave;

import java.util.List;

@Repository
public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long> {
    List<EmployeeLeave> findByEmployeeId(Long employeeId);
    boolean existsByIdAndEmployeeId(Long id, Long employeeId);
} 