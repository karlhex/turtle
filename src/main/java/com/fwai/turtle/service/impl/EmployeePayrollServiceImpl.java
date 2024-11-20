package com.fwai.turtle.service.impl;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeePayrollDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.EmployeePayroll;
import com.fwai.turtle.persistence.repository.EmployeePayrollRepository;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.service.interfaces.IEmployeePayrollService;
import com.fwai.turtle.persistence.mapper.EmployeePayrollMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EmployeePayrollServiceImpl implements IEmployeePayrollService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeePayrollRepository employeePayrollRepository;

    @Autowired
    private EmployeePayrollMapper employeePayrollMapper;

    @Override
    public Result<EmployeePayrollDTO> add(Long employeeId, EmployeePayrollDTO payrollDTO) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
                
        EmployeePayroll payroll = employeePayrollMapper.toEntity(payrollDTO);
        payroll.setEmployeeId(employee.getId());
        payroll = employeePayrollRepository.save(payroll);
            
        return Result.success(employeePayrollMapper.toDTO(payroll));
    }
        
    @Override
    public Result<EmployeePayrollDTO> update(Long employeeId, Long payrollId, EmployeePayrollDTO payrollDTO) {
        EmployeePayroll payroll = employeePayrollRepository.findByIdAndEmployeeId(payrollId, employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("工资记录不存在"));
                
        employeePayrollMapper.updateEntity(payrollDTO, payroll);
        payroll = employeePayrollRepository.save(payroll);
            
        return Result.success(employeePayrollMapper.toDTO(payroll));
    }
        
    @Override
    public Result<Void> delete(Long employeeId, Long payrollId) {
        if (!employeePayrollRepository.existsByIdAndEmployeeId(payrollId, employeeId)) {
            throw new ResourceNotFoundException("工资记录不存在");
        }
        employeePayrollRepository.deleteById(payrollId);
        return Result.success(null);
    }
}
