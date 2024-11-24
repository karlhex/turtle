package com.fwai.turtle.service.impl;

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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeEducationServiceImpl implements IEmployeeEducationService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeEducationRepository employeeEducationRepository;

    @Autowired
    private EmployeeEducationMapper employeeEducationMapper;

    @Override
    public EmployeeEducationDTO add(Long employeeId, EmployeeEducationDTO educationDTO) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));
                
        EmployeeEducation education = employeeEducationMapper.toEntity(educationDTO);
        education.setEmployeeId(employee.getId());
        education = employeeEducationRepository.save(education);
            
        return employeeEducationMapper.toDTO(education);
    }
        
    @Override
    public EmployeeEducationDTO update(Long employeeId, Long educationId, EmployeeEducationDTO educationDTO) {
        EmployeeEducation education = employeeEducationRepository.findByIdAndEmployeeId(educationId, employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("教育信息不存在"));
                
        employeeEducationMapper.updateEntity(educationDTO, education);
        education = employeeEducationRepository.save(education);
            
        return employeeEducationMapper.toDTO(education);
    }
        
    @Override
    public void delete(Long employeeId, Long educationId) {
        if (!employeeEducationRepository.existsByIdAndEmployeeId(educationId, employeeId)) {
            throw new ResourceNotFoundException("教育信息不存在");
        }
        employeeEducationRepository.deleteById(educationId);
    }

    @Override
    public List<EmployeeEducationDTO> getByEmployeeId(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new ResourceNotFoundException("员工不存在");
        }
        return employeeEducationRepository.findByEmployeeId(employeeId)
            .stream()
            .map(employeeEducationMapper::toDTO)
            .collect(Collectors.toList());
    }
}
