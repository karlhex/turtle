package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Page<Department> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
