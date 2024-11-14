package com.fwai.turtle.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwai.turtle.persistence.entity.EmployeeEducation;

import java.util.Optional;

@Repository
public interface EmployeeEducationRepository extends JpaRepository<EmployeeEducation, Long> {
    Optional<EmployeeEducation> findByIdAndEmployeeId(Long id, Long employeeId);
    boolean existsByIdAndEmployeeId(Long id, Long employeeId);
} 