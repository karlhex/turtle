package com.fwai.turtle.modules.organization.service.impl;

import java.util.stream.Collectors;

import com.fwai.turtle.modules.organization.service.EmployeeJobHistoryService;

import com.fwai.turtle.modules.organization.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.modules.organization.entity.EmployeeJobHistory;
import com.fwai.turtle.modules.organization.repository.EmployeeJobHistoryRepository;
import com.fwai.turtle.modules.organization.repository.EmployeeRepository;
import com.fwai.turtle.modules.organization.mapper.EmployeeJobHistoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeJobHistoryServiceImpl implements EmployeeJobHistoryService {
        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private EmployeeJobHistoryRepository jobHistoryRepository;

        @Autowired
        private EmployeeJobHistoryMapper jobHistoryMapper;

        @Transactional
    public EmployeeJobHistoryDTO add(Long employeeId, EmployeeJobHistoryDTO jobHistoryDTO) {

        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));

        EmployeeJobHistory jobHistory = jobHistoryMapper.toEntity(jobHistoryDTO);
        jobHistory.setEmployee(employee);
        jobHistory = jobHistoryRepository.save(jobHistory);
        
        return jobHistoryMapper.toDTO(jobHistory);
    }

    @Transactional
    public EmployeeJobHistoryDTO update(Long employeeId, Long jobHistoryId, 
            EmployeeJobHistoryDTO jobHistoryDTO) {
        EmployeeJobHistory jobHistory = jobHistoryRepository.findById(jobHistoryId)
            .orElseThrow(() -> new ResourceNotFoundException("工作历史记录不存在"));
        
        if (!jobHistory.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("工作历史记录不属于该员工");
        }

        // 更新字段
        jobHistory.setCompanyName(jobHistoryDTO.getCompanyName());
        jobHistory.setPosition(jobHistoryDTO.getPosition());
        jobHistory.setStartDate(jobHistoryDTO.getStartDate());
        jobHistory.setEndDate(jobHistoryDTO.getEndDate());
        jobHistory.setDepartment(jobHistoryDTO.getDepartment());
        jobHistory.setJobDescription(jobHistoryDTO.getJobDescription());
        jobHistory.setAchievements(jobHistoryDTO.getAchievements());
        jobHistory.setLeavingReason(jobHistoryDTO.getLeavingReason());
        jobHistory.setReferenceContact(jobHistoryDTO.getReferenceContact());
        jobHistory.setRemarks(jobHistoryDTO.getRemarks());
        
        jobHistory = jobHistoryRepository.save(jobHistory);
        return jobHistoryMapper.toDTO(jobHistory);
    }

    public List<EmployeeJobHistoryDTO> getByEmployeeId(Long employeeId) {
        return jobHistoryRepository.findByEmployeeId(employeeId)
            .stream()
            .map(jobHistoryMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long employeeId, Long jobHistoryId) {
        EmployeeJobHistory jobHistory = jobHistoryRepository.findById(jobHistoryId)
            .orElseThrow(() -> new ResourceNotFoundException("工作历史记录不存在"));
        
        if (!jobHistory.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("工作历史记录不属于该员工");
        }

        jobHistoryRepository.delete(jobHistory);
    }

}
