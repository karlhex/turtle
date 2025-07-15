package com.fwai.turtle.modules.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.modules.organization.entity.EmployeeEducation;

import java.util.Optional;
import java.util.List;

@Repository
public interface EmployeeEducationRepository extends JpaRepository<EmployeeEducation, Long> {
    Optional<EmployeeEducation> findByIdAndEmployeeId(Long id, Long employeeId);
    boolean existsByIdAndEmployeeId(Long id, Long employeeId);
    List<EmployeeEducation> findByEmployeeId(Long employeeId);
}