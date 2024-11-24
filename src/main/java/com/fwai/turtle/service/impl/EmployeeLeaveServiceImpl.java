package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.EmployeeLeaveDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.EmployeeLeave;
import com.fwai.turtle.persistence.repository.EmployeeLeaveRepository;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.service.interfaces.IEmployeeLeaveService;
import com.fwai.turtle.persistence.mapper.EmployeeLeaveMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EmployeeLeaveServiceImpl implements IEmployeeLeaveService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeLeaveRepository employeeLeaveRepository;

    @Autowired
    private EmployeeLeaveMapper employeeLeaveMapper;

    @Override
    public EmployeeLeaveDTO add(Long employeeId, EmployeeLeaveDTO LeaveDTO) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
                
        EmployeeLeave leave = employeeLeaveMapper.toEntity(LeaveDTO);
        leave.setEmployeeId(employee.getId());
        leave = employeeLeaveRepository.save(leave);
            
        return employeeLeaveMapper.toDTO(leave);
    }
        
    @Override
    public EmployeeLeaveDTO update(Long employeeId, Long leaveId, EmployeeLeaveDTO leaveDTO) {
        EmployeeLeave leave = employeeLeaveRepository.findById(leaveId)
            .orElseThrow(() -> new ResourceNotFoundException("请假记录不存在"));
                
        employeeLeaveMapper.updateEntity(leaveDTO,leave);
        leave = employeeLeaveRepository.save(leave);
            
        return employeeLeaveMapper.toDTO(leave);
    }
        
    @Override
    public void delete(Long employeeId, Long leaveId) {
        if (!employeeLeaveRepository.existsByIdAndEmployeeId(leaveId, employeeId)) {
            throw new ResourceNotFoundException("请假记录不存在");
        }
        employeeLeaveRepository.deleteById(leaveId);
    }
}
