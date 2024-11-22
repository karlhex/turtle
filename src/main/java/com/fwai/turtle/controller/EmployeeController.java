package com.fwai.turtle.controller;

import com.fwai.turtle.common.PageResponse;
import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.dto.EmployeeEducationDTO;
import com.fwai.turtle.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.dto.EmployeeLeaveDTO;
import com.fwai.turtle.dto.EmployeePayrollDTO;
import com.fwai.turtle.service.EmployeeService;
import com.fwai.turtle.service.impl.EmployeeAttendanceServiceImpl;
import com.fwai.turtle.service.interfaces.IEmployeeLeaveService;
import com.fwai.turtle.service.interfaces.IEmployeePayrollService;
import com.fwai.turtle.service.interfaces.IEmployeeEducationService;
import com.fwai.turtle.service.interfaces.EmployeeJobHistoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final IEmployeeEducationService employeeEducationService;
    private final EmployeeAttendanceServiceImpl employeeAttendanceService;
    private final IEmployeeLeaveService employeeLeaveService;
    private final IEmployeePayrollService employeePayrollService;
    @Autowired
    private final EmployeeJobHistoryService employeeJobHistoryService;

    @PostMapping
    public ResponseEntity<Result<EmployeeDTO>> create(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.create(employeeDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<EmployeeDTO>> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.update(id, employeeDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<EmployeeDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Result<PageResponse<EmployeeDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        return ResponseEntity.ok(employeeService.getAll(page, size, sortBy, direction));
    }

    @GetMapping("/search")
    public ResponseEntity<Result<PageResponse<EmployeeDTO>>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.search(query, page, size));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        return employeeService.delete(id);
    }

    @PostMapping("/{employeeId}/educations")
    public Result<EmployeeEducationDTO> addEducation(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeEducationDTO educationDTO) {
        return employeeEducationService.add(employeeId, educationDTO);
    }
    
    @PutMapping("/{employeeId}/educations/{educationId}")
    public Result<EmployeeEducationDTO> updateEducation(
            @PathVariable Long employeeId,
            @PathVariable Long educationId,
            @Valid @RequestBody EmployeeEducationDTO educationDTO) {
        return employeeEducationService.update(employeeId, educationId, educationDTO);
    }
    
    @DeleteMapping("/{employeeId}/educations/{educationId}")
    public void deleteEducation(
            @PathVariable Long employeeId,
            @PathVariable Long educationId) {
        employeeEducationService.delete(employeeId, educationId);
    }

    @PostMapping("/{employeeId}/attendance")
    public Result<EmployeeAttendanceDTO> addAttendance(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeAttendanceDTO attendanceDTO) {
        return employeeAttendanceService.add(employeeId, attendanceDTO);
    }

    @PutMapping("/{employeeId}/attendance/{attendanceId}")
    public Result<EmployeeAttendanceDTO> updateAttendance(
            @PathVariable Long employeeId,
            @PathVariable Long attendanceId,
            @Valid @RequestBody EmployeeAttendanceDTO attendanceDTO) {
        return employeeAttendanceService.update(employeeId, attendanceId, attendanceDTO);
    }

    @GetMapping("/{employeeId}/attendance")
    public Result<List<EmployeeAttendanceDTO>> getAttendance(@PathVariable Long employeeId) {
        return employeeAttendanceService.getByEmployeeId(employeeId);
    }

    @DeleteMapping("/{employeeId}/attendance/{attendanceId}")
    public Result<Void> deleteAttendance(
            @PathVariable Long employeeId,
            @PathVariable Long attendanceId) {
        return employeeAttendanceService.delete(employeeId, attendanceId);
    }

    @GetMapping("/{employeeId}/job-history")
    public Result<List<EmployeeJobHistoryDTO>> getJobHistory(@PathVariable Long employeeId) {
        return employeeJobHistoryService.getByEmployeeId(employeeId);
    }

    @PostMapping("/{employeeId}/job-history")
    public Result<EmployeeJobHistoryDTO> createJobHistory(@PathVariable Long employeeId, @RequestBody EmployeeJobHistoryDTO jobHistoryDTO) {
        jobHistoryDTO.setEmployeeId(employeeId);
        return employeeJobHistoryService.add(employeeId, jobHistoryDTO);
    }

    @PutMapping("/{employeeId}/job-history/{id}")
    public Result<EmployeeJobHistoryDTO> updateJobHistory(@PathVariable Long employeeId, @PathVariable Long id, @RequestBody EmployeeJobHistoryDTO jobHistoryDTO) {
        jobHistoryDTO.setEmployeeId(employeeId);
        return employeeJobHistoryService.update(employeeId, id, jobHistoryDTO);
    }

    @DeleteMapping("/{employeeId}/job-history/{id}")
    public Result<Void> deleteJobHistory(@PathVariable Long employeeId, @PathVariable Long id) {
        return employeeJobHistoryService.delete(employeeId, id);
    }

    // Employee Leave APIs
    @PostMapping("/{employeeId}/leaves")
    public ResponseEntity<Result<EmployeeLeaveDTO>> addLeave(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeLeaveDTO leaveDTO) {
        return ResponseEntity.ok(employeeLeaveService.add(employeeId, leaveDTO));
    }

    @PutMapping("/{employeeId}/leaves/{leaveId}")
    public ResponseEntity<Result<EmployeeLeaveDTO>> updateLeave(
            @PathVariable Long employeeId,
            @PathVariable Long leaveId,
            @Valid @RequestBody EmployeeLeaveDTO leaveDTO) {
        return ResponseEntity.ok(employeeLeaveService.update(employeeId, leaveId, leaveDTO));
    }

    @DeleteMapping("/{employeeId}/leaves/{leaveId}")
    public ResponseEntity<Result<Void>> deleteLeave(
            @PathVariable Long employeeId,
            @PathVariable Long leaveId) {
        return ResponseEntity.ok(employeeLeaveService.delete(employeeId, leaveId));
    }

    // Employee Payroll APIs
    @PostMapping("/{employeeId}/payrolls")
    public ResponseEntity<Result<EmployeePayrollDTO>> addPayroll(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeePayrollDTO payrollDTO) {
        return ResponseEntity.ok(employeePayrollService.add(employeeId, payrollDTO));
    }

    @PutMapping("/{employeeId}/payrolls/{payrollId}")
    public ResponseEntity<Result<EmployeePayrollDTO>> updatePayroll(
            @PathVariable Long employeeId,
            @PathVariable Long payrollId,
            @Valid @RequestBody EmployeePayrollDTO payrollDTO) {
        return ResponseEntity.ok(employeePayrollService.update(employeeId, payrollId, payrollDTO));
    }

    @DeleteMapping("/{employeeId}/payrolls/{payrollId}")
    public ResponseEntity<Result<Void>> deletePayroll(
            @PathVariable Long employeeId,
            @PathVariable Long payrollId) {
        return ResponseEntity.ok(employeePayrollService.delete(employeeId, payrollId));
    }
}