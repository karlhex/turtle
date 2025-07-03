package com.fwai.turtle.service.impl;

import java.time.LocalDate;
import java.util.stream.Collectors;

import com.fwai.turtle.service.interfaces.EmployeeAttendanceService;

import com.fwai.turtle.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.exception.DuplicateRecordException;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.EmployeeAttendance;
import com.fwai.turtle.persistence.repository.EmployeeAttendanceRepository;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.persistence.mapper.EmployeeAttendanceMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeAttendanceServiceImpl implements EmployeeAttendanceService {
        @Autowired
        private EmployeeRepository employeeRepository;

        @Autowired
        private EmployeeAttendanceRepository attendanceRepository;

        @Autowired
        private EmployeeAttendanceMapper attendanceMapper;

        @Transactional
    public EmployeeAttendanceDTO add(Long employeeId, EmployeeAttendanceDTO attendanceDTO) {
        // 检查是否已存在当天的考勤记录
        if (attendanceRepository.existsByEmployeeIdAndAttendanceDate(
                employeeId, attendanceDTO.attendanceDate())) {
            throw new DuplicateRecordException("该日期已存在考勤记录");
        }

        // 检查考勤日期是否是将来日期
        if (attendanceDTO.attendanceDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("不能添加将来日期的考勤记录");
        }

        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("员工不存在"));

        EmployeeAttendance attendance = attendanceMapper.toEntity(attendanceDTO);
        attendance.setEmployee(employee);
        attendance = attendanceRepository.save(attendance);
        
        return attendanceMapper.toDTO(attendance);
    }

    @Transactional
    public EmployeeAttendanceDTO update(Long employeeId, Long attendanceId, 
            EmployeeAttendanceDTO attendanceDTO) {
        EmployeeAttendance attendance = attendanceRepository.findById(attendanceId)
            .orElseThrow(() -> new ResourceNotFoundException("考勤记录不存在"));
        
        if (!attendance.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("考勤记录不属于该员工");
        }

        // 检查考勤日期是否是将来日期
        if (attendanceDTO.attendanceDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("不能更新为将来日期的考勤记录");
        }

        // 更新字段
        attendance.setAttendanceDate(attendanceDTO.attendanceDate());
        attendance.setCheckInTime(attendanceDTO.checkInTime());
        attendance.setCheckOutTime(attendanceDTO.checkOutTime());
        attendance.setStatus(attendanceDTO.status());
        attendance.setRemarks(attendanceDTO.remarks());
        
        attendance = attendanceRepository.save(attendance);
        return attendanceMapper.toDTO(attendance);
    }

    public List<EmployeeAttendanceDTO> getByEmployeeId(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId)
            .stream()
            .map(attendanceMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long employeeId, Long attendanceId) {
        EmployeeAttendance attendance = attendanceRepository.findById(attendanceId)
            .orElseThrow(() -> new ResourceNotFoundException("考勤记录不存在"));
        
        if (!attendance.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("考勤记录不属于该员工");
        }

        attendanceRepository.delete(attendance);
    }
}
