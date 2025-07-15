package com.fwai.turtle.modules.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.modules.organization.entity.EmployeeJobHistory;

import java.util.List;

@Repository
public interface EmployeeJobHistoryRepository extends JpaRepository<EmployeeJobHistory, Long> {
    List<EmployeeJobHistory> findByEmployeeId(Long employeeId);
 
} 