package com.fwai.turtle.service;

import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.EmployeeEducation;
import com.fwai.turtle.persistence.entity.EmployeeJobHistory;
import com.fwai.turtle.persistence.mapper.EmployeeEducationMapper;
import com.fwai.turtle.persistence.mapper.EmployeeJobHistoryMapper;
import com.fwai.turtle.persistence.mapper.EmployeeMapper;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.types.EmployeeStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fwai.turtle.exception.DuplicateRecordException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeEducationMapper employeeEducationMapper;
    private final EmployeeJobHistoryMapper employeeJobHistoryMapper;

    @Transactional
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        log.debug("create employee: {}", employeeDTO);
        Employee employeeEntity = employeeMapper.toEntity(employeeDTO);
        if (employeeRepository.existsByEmployeeNumber(employeeEntity.getEmployeeNumber())) {
            throw new DuplicateRecordException("员工编号已存在");
        }

        // 处理教育经历
        if (employeeDTO.getEducations() != null) {
            List<EmployeeEducation> educations = employeeDTO.getEducations().stream()
                .map(educationDTO -> {
                    EmployeeEducation education = employeeEducationMapper.toEntity(educationDTO);
                    education.setEmployee(employeeEntity);
                    return education;
                })
                .collect(Collectors.toList());
            employeeEntity.setEducations(educations);
        }

        // 处理工作经历
        if (employeeDTO.getJobHistories() != null) {
            List<EmployeeJobHistory> jobHistories = employeeDTO.getJobHistories().stream()
                .map(jobHistoryDTO -> {
                    EmployeeJobHistory jobHistory = employeeJobHistoryMapper.toEntity(jobHistoryDTO);
                    jobHistory.setEmployee(employeeEntity);
                    return jobHistory;
                })
                .collect(Collectors.toList());
            employeeEntity.setJobHistories(jobHistories);
        }

        Employee savedEmployee = employeeRepository.save(employeeEntity);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO employeeDTO) {
        log.debug("update employee: {}", employeeDTO);
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
        
        employeeMapper.updateEntity(employeeDTO, employee);

        // 处理教育经历
        if (employeeDTO.getEducations() != null) {
            employee.getEducations().clear();
            List<EmployeeEducation> updatedEducations = employeeDTO.getEducations().stream()
                .map(educationDTO -> {
                    EmployeeEducation education = employeeEducationMapper.toEntity(educationDTO);
                    education.setEmployee(employee);
                    return education;
                })
                .collect(Collectors.toList());
            employee.getEducations().addAll(updatedEducations);
        }

        // 处理工作经历
        if (employeeDTO.getJobHistories() != null) {
            employee.getJobHistories().clear();
            List<EmployeeJobHistory> updatedJobHistories = employeeDTO.getJobHistories().stream()
                .map(jobHistoryDTO -> {
                    EmployeeJobHistory jobHistory = employeeJobHistoryMapper.toEntity(jobHistoryDTO);
                    jobHistory.setEmployee(employee);
                    return jobHistory;
                })
                .collect(Collectors.toList());
            employee.getJobHistories().addAll(updatedJobHistories);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        log.debug("update employee: {}", savedEmployee);
        return employeeMapper.toDTO(savedEmployee);
    }
        
    public EmployeeDTO getById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
        return employeeMapper.toDTO(employee);
    }

    public Page<EmployeeDTO> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        return employeePage.map(employeeMapper::toDTO);
    }
        
    public Page<EmployeeDTO> search(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage;
        
        if (StringUtils.hasText(query)) {
            employeePage = employeeRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentNameContainingIgnoreCase(
                query, query, query, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }
        
        return employeePage.map(employeeMapper::toDTO);
    }

    public List<EmployeeDTO> getUnmappedEmployees() {
        List<Employee> employees = employeeRepository.findByUserIsNull();
        return employees.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getActiveEmployees() {
        List<Employee> employees = employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
        return employees.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<EmployeeDTO> getEmployeesByStatus(EmployeeStatus status) {
        List<Employee> employees = employeeRepository.findByStatus(status);
        return employees.stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("员工不存在");
        }
        employeeRepository.deleteById(id);
    }

    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        return create(employeeDTO);
    }

    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        return update(id, employeeDTO);
    }

    public void deleteEmployee(Long id) {
        delete(id);
    }

    public EmployeeDTO getEmployee(Long id) {
        return getById(id);
    }

    public List<EmployeeDTO> getAllEmployees() {
        return getAll(0, Integer.MAX_VALUE, "id", "asc").getContent();
    }

    public List<EmployeeDTO> getUnassignedEmployees() {
        return getUnmappedEmployees();
    }

    public Page<EmployeeDTO> searchEmployees(String query, Pageable pageable) {
        return search(query, pageable.getPageNumber(), pageable.getPageSize());
    }

    public boolean existsByEmployeeNumber(String employeeNumber) {
        return employeeRepository.existsByEmployeeNumber(employeeNumber);
    }

    public boolean existsByIdNumber(String idNumber) {
        return employeeRepository.existsByIdNumber(idNumber);
    }

    public boolean existsByEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public Optional<EmployeeDTO> findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .map(employeeMapper::toDTO);
    }
}