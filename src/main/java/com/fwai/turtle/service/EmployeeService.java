package com.fwai.turtle.service;

import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.mapper.EmployeeMapper;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.common.PageResponse;
import com.fwai.turtle.common.Result;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import com.fwai.turtle.exception.DuplicateRecordException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Transactional
    public Result<EmployeeDTO> create(EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        if (employeeRepository.existsByEmployeeNumber(employee.getEmployeeNumber())) {
            throw new DuplicateRecordException("员工编号已存在");
        }

        employee = employeeRepository.save(employee);
        return Result.success(employeeMapper.toDTO(employee));
    }

    @Transactional
    public Result<EmployeeDTO> update(Long id, EmployeeDTO employeeDTO) {
        log.debug("update employee: {}", employeeDTO);
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
        
        employeeMapper.updateEntity(employeeDTO, employee);
        employee = employeeRepository.save(employee);
        log.debug("update employee: {}", employee);
        return Result.success(employeeMapper.toDTO(employee));
    }

    public Result<EmployeeDTO> getById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
        return Result.success(employeeMapper.toDTO(employee));
    }

    public Result<PageResponse<EmployeeDTO>> getAll(int page, int size, String sortBy, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Employee> employeePage = employeeRepository.findAll(pageable);
        List<EmployeeDTO> employeeDTOs = employeePage.getContent().stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
        
        PageResponse<EmployeeDTO> pageResponse = PageResponse.of(
            employeeDTOs,
            employeePage.getNumber(),
            employeePage.getSize(),
            employeePage.getTotalElements(),
            employeePage.getTotalPages()
        );
        
        return Result.success(pageResponse);
    }

    public Result<PageResponse<EmployeeDTO>> search(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employeePage;
        
        if (StringUtils.hasText(query)) {
            employeePage = employeeRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrDepartmentNameContainingIgnoreCase(
                query, query, query, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }
        
        List<EmployeeDTO> employeeDTOs = employeePage.getContent().stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
        
        PageResponse<EmployeeDTO> pageResponse = PageResponse.of(
            employeeDTOs,
            employeePage.getNumber(),
            employeePage.getSize(),
            employeePage.getTotalElements(),
            employeePage.getTotalPages()
        );
        
        return Result.success(pageResponse);
    }

    @Transactional
    public Result<Void> delete(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("员工不存在");
        }
        employeeRepository.deleteById(id);
        return Result.success(null);
    }
}