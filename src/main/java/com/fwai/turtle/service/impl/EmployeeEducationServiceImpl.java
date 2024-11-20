package com.fwai.turtle.service.impl;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeeEducationDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.EmployeeEducation;
import com.fwai.turtle.persistence.repository.EmployeeEducationRepository;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.service.interfaces.IEmployeeEducationService;
import com.fwai.turtle.persistence.mapper.EmployeeEducationMapper;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EmployeeEducationServiceImpl implements IEmployeeEducationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeEducationRepository employeeEducationRepository;

    @Autowired
    private EmployeeEducationMapper employeeEducationMapper;

    @Override
    public Result<EmployeeEducationDTO> add(Long employeeId, EmployeeEducationDTO educationDTO) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
                
        EmployeeEducation education = employeeEducationMapper.toEntity(educationDTO);
        education.setEmployeeId(employee.getId());
        education = employeeEducationRepository.save(education);
            
        return Result.success(employeeEducationMapper.toDTO(education));
    }
        
    @Override
    public Result<EmployeeEducationDTO> update(Long employeeId, Long educationId, EmployeeEducationDTO educationDTO) {
        EmployeeEducation education = employeeEducationRepository.findByIdAndEmployeeId(educationId, employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("教育信息不存在"));
                
        employeeEducationMapper.updateEntity(educationDTO, education);
        education = employeeEducationRepository.save(education);
            
        return Result.success(employeeEducationMapper.toDTO(education));
    }
        
    @Override
    public Result<Void> delete(Long employeeId, Long educationId) {
        if (!employeeEducationRepository.existsByIdAndEmployeeId(educationId, employeeId)) {
            throw new ResourceNotFoundException("教育信息不存在");
        }
        employeeEducationRepository.deleteById(educationId);
        return Result.success(null);
    }
}
