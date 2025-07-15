package com.fwai.turtle.modules.organization.service.impl;

import com.fwai.turtle.modules.organization.dto.EmployeePayrollDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.modules.organization.entity.EmployeePayroll;
import com.fwai.turtle.modules.organization.repository.EmployeePayrollRepository;
import com.fwai.turtle.modules.organization.repository.EmployeeRepository;
import com.fwai.turtle.modules.organization.service.EmployeePayrollService;
import com.fwai.turtle.modules.organization.mapper.EmployeePayrollMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EmployeePayrollServiceImpl implements EmployeePayrollService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeePayrollRepository employeePayrollRepository;

    @Autowired
    private EmployeePayrollMapper employeePayrollMapper;

    @Override
    public EmployeePayrollDTO add(Long employeeId, EmployeePayrollDTO payrollDTO) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
                
        EmployeePayroll payroll = employeePayrollMapper.toEntity(payrollDTO);
        payroll.setEmployeeId(employee.getId());
        payroll = employeePayrollRepository.save(payroll);
            
        return employeePayrollMapper.toDTO(payroll);
    }
        
    @Override
    public EmployeePayrollDTO update(Long employeeId, Long payrollId, EmployeePayrollDTO payrollDTO) {
        EmployeePayroll payroll = employeePayrollRepository.findByIdAndEmployeeId(payrollId, employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("工资记录不存在"));
                
        employeePayrollMapper.updateEntity(payrollDTO, payroll);
        payroll = employeePayrollRepository.save(payroll);
            
        return employeePayrollMapper.toDTO(payroll);
    }
        
    @Override
    public void delete(Long employeeId, Long payrollId) {
        if (!employeePayrollRepository.existsByIdAndEmployeeId(payrollId, employeeId)) {
            throw new ResourceNotFoundException("工资记录不存在");
        }
        employeePayrollRepository.deleteById(payrollId);
    }
}
