package com.fwai.turtle.service.impl;

import java.util.stream.Collectors;

import com.fwai.turtle.service.interfaces.EmployeeJobHistoryService;

import com.fwai.turtle.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.EmployeeJobHistory;
import com.fwai.turtle.persistence.repository.EmployeeJobHistoryRepository;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.persistence.mapper.EmployeeJobHistoryMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fwai.turtle.common.Result;

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
    public Result<EmployeeJobHistoryDTO> add(Long employeeId, EmployeeJobHistoryDTO jobHistoryDTO) {

        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));

        EmployeeJobHistory jobHistory = jobHistoryMapper.toEntity(jobHistoryDTO);
        jobHistory.setEmployeeId(employee.getId());
        jobHistory = jobHistoryRepository.save(jobHistory);
        
        return Result.success(jobHistoryMapper.toDTO(jobHistory));
    }

    @Transactional
    public Result<EmployeeJobHistoryDTO> update(Long employeeId, Long jobHistoryId, 
            EmployeeJobHistoryDTO jobHistoryDTO) {
        EmployeeJobHistory jobHistory = jobHistoryRepository.findById(jobHistoryId)
            .orElseThrow(() -> new ResourceNotFoundException("工作历史记录不存在"));
        
        if (!jobHistory.getEmployeeId().equals(employeeId)) {
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
        return Result.success(jobHistoryMapper.toDTO(jobHistory));
    }

    public Result<List<EmployeeJobHistoryDTO>> getByEmployeeId(Long employeeId) {
        List<EmployeeJobHistoryDTO> jobHistories = jobHistoryRepository.findByEmployeeId(employeeId)
            .stream()
            .map(jobHistoryMapper::toDTO)
            .collect(Collectors.toList());
        return Result.success(jobHistories);
    }

    @Transactional
    public Result<Void> delete(Long employeeId, Long jobHistoryId) {
        EmployeeJobHistory jobHistory = jobHistoryRepository.findById(jobHistoryId)
            .orElseThrow(() -> new ResourceNotFoundException("工作历史记录不存在"));
        
        if (!jobHistory.getEmployeeId().equals(employeeId)) {
            throw new IllegalArgumentException("工作历史记录不属于该员工");
        }

        jobHistoryRepository.delete(jobHistory);
        return Result.success(null);
    }

}
