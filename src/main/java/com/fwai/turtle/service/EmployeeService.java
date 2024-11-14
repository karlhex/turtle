package com.fwai.turtle.service;

import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.mapper.EmployeeMapper;
import com.fwai.turtle.persistence.repository.EmployeeRepository;

import com.fwai.turtle.common.Result;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.fwai.turtle.exception.DuplicateRecordException;

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
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
        
        employeeMapper.updateEntity(employeeDTO, employee);
        employee = employeeRepository.save(employee);
        return Result.success(employeeMapper.toDTO(employee));
    }

    public Result<EmployeeDTO> getById(Long id) {
        Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
        return Result.success(employeeMapper.toDTO(employee));
    }

    public Result<List<EmployeeDTO>> getAll(int page, int size) {
        List<EmployeeDTO> employees = employeeRepository.findAll(PageRequest.of(page, size))
            .stream()
            .map(employeeMapper::toDTO)
            .collect(Collectors.toList());
        return Result.success(employees);
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