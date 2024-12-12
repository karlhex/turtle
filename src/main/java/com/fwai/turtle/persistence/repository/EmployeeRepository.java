package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByUserIsNull();

    List<Employee> findByIsActive(Boolean isActive);

    boolean existsByEmployeeNumber(String employeeNumber);
    
    Page<Employee> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentNameContainingIgnoreCase(
        String name, String email, String departmentName, Pageable pageable);
}