package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.types.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    List<Employee> findByUserIsNull();

    List<Employee> findByStatus(EmployeeStatus status);

    Optional<Employee> findByEmployeeNumber(String employeeNumber);
    Optional<Employee> findByIdNumber(String idNumber);
    Optional<Employee> findByEmail(String email);
    boolean existsByEmployeeNumber(String employeeNumber);
    boolean existsByIdNumber(String idNumber);
    boolean existsByEmail(String email);
    
    Page<Employee> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentNameContainingIgnoreCase(
        String name, String email, String departmentName, Pageable pageable);
}