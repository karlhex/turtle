package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final IEmployeeEducationService employeeEducationService;
    private final EmployeeAttendanceServiceImpl employeeAttendanceService;
    private final IEmployeeLeaveService employeeLeaveService;
    private final IEmployeePayrollService employeePayrollService;
    @Autowired
    private final EmployeeJobHistoryService employeeJobHistoryService;

    @PostMapping
    public ApiResponse<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ApiResponse.ok(employeeService.create(employeeDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ApiResponse.ok(employeeService.update(id, employeeDTO));
    }

    @GetMapping("/{id}")
    public ApiResponse<EmployeeDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(employeeService.getById(id));
    }

    @GetMapping
    public ApiResponse<Page<EmployeeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        return ApiResponse.ok(employeeService.getAll(page, size, sortBy, direction));
    }

    @GetMapping("/search")
    public ApiResponse<Page<EmployeeDTO>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(employeeService.search(query, page, size));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{employeeId}/educations")
    public ApiResponse<EmployeeEducationDTO> addEducation(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeEducationDTO educationDTO) {
        return ApiResponse.ok(employeeEducationService.add(employeeId, educationDTO));
    }
    
    @PutMapping("/{employeeId}/educations/{educationId}")
    public ApiResponse<EmployeeEducationDTO> updateEducation(
            @PathVariable Long employeeId,
            @PathVariable Long educationId,
            @Valid @RequestBody EmployeeEducationDTO educationDTO) {
        return ApiResponse.ok(employeeEducationService.update(employeeId, educationId, educationDTO));
    }
    
    @DeleteMapping("/{employeeId}/educations/{educationId}")
    public ApiResponse<Void> deleteEducation(
            @PathVariable Long employeeId,
            @PathVariable Long educationId) {
        employeeEducationService.delete(employeeId, educationId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{employeeId}/educations")
    public ApiResponse<List<EmployeeEducationDTO>> getEducations(@PathVariable Long employeeId) {
        return ApiResponse.ok(employeeEducationService.getByEmployeeId(employeeId));
    }

    @PostMapping("/{employeeId}/attendance")
    public ApiResponse<EmployeeAttendanceDTO> addAttendance(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeAttendanceDTO attendanceDTO) {
        return ApiResponse.ok(employeeAttendanceService.add(employeeId, attendanceDTO));
    }

    @PutMapping("/{employeeId}/attendance/{attendanceId}")
    public ApiResponse<EmployeeAttendanceDTO> updateAttendance(
            @PathVariable Long employeeId,
            @PathVariable Long attendanceId,
            @Valid @RequestBody EmployeeAttendanceDTO attendanceDTO) {
        return ApiResponse.ok(employeeAttendanceService.update(employeeId, attendanceId, attendanceDTO));
    }

    @GetMapping("/{employeeId}/attendance")
    public ApiResponse<List<EmployeeAttendanceDTO>> getAttendance(@PathVariable Long employeeId) {
        return ApiResponse.ok(employeeAttendanceService.getByEmployeeId(employeeId));
    }

    @DeleteMapping("/{employeeId}/attendance/{attendanceId}")
    public ApiResponse<Void> deleteAttendance(
            @PathVariable Long employeeId,
            @PathVariable Long attendanceId) {
        employeeAttendanceService.delete(employeeId, attendanceId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{employeeId}/job-history")
    public ApiResponse<List<EmployeeJobHistoryDTO>> getJobHistory(@PathVariable Long employeeId) {
        return ApiResponse.ok(employeeJobHistoryService.getByEmployeeId(employeeId));
    }

    @PostMapping("/{employeeId}/job-history")
    public ApiResponse<EmployeeJobHistoryDTO> createJobHistory(@PathVariable Long employeeId, @RequestBody EmployeeJobHistoryDTO jobHistoryDTO) {
        jobHistoryDTO.setEmployeeId(employeeId);
        return ApiResponse.ok(employeeJobHistoryService.add(employeeId, jobHistoryDTO));
    }

    @PutMapping("/{employeeId}/job-history/{id}")
    public ApiResponse<EmployeeJobHistoryDTO> updateJobHistory(@PathVariable Long employeeId, @PathVariable Long id, @RequestBody EmployeeJobHistoryDTO jobHistoryDTO) {
        jobHistoryDTO.setEmployeeId(employeeId);
        return ApiResponse.ok(employeeJobHistoryService.update(employeeId, id, jobHistoryDTO));
    }

    @DeleteMapping("/{employeeId}/job-history/{id}")
    public ApiResponse<Void> deleteJobHistory(@PathVariable Long employeeId, @PathVariable Long id) {
        employeeJobHistoryService.delete(employeeId, id);
        return ApiResponse.ok(null);
    }

    // Employee Leave APIs
    @PostMapping("/{employeeId}/leaves")
    public ApiResponse<EmployeeLeaveDTO> addLeave(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeLeaveDTO leaveDTO) {
        return ApiResponse.ok(employeeLeaveService.add(employeeId, leaveDTO));
    }

    @PutMapping("/{employeeId}/leaves/{leaveId}")
    public ApiResponse<EmployeeLeaveDTO> updateLeave(
            @PathVariable Long employeeId,
            @PathVariable Long leaveId,
            @Valid @RequestBody EmployeeLeaveDTO leaveDTO) {
        return ApiResponse.ok(employeeLeaveService.update(employeeId, leaveId, leaveDTO));
    }

    @DeleteMapping("/{employeeId}/leaves/{leaveId}")
    public ApiResponse<Void> deleteLeave(
            @PathVariable Long employeeId,
            @PathVariable Long leaveId) {
        employeeLeaveService.delete(employeeId, leaveId);
        return ApiResponse.ok(null);
    }

    // Employee Payroll APIs
    @PostMapping("/{employeeId}/payrolls")
    public ApiResponse<EmployeePayrollDTO> addPayroll(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeePayrollDTO payrollDTO) {
        return ApiResponse.ok(employeePayrollService.add(employeeId, payrollDTO));
    }

    @PutMapping("/{employeeId}/payrolls/{payrollId}")
    public ApiResponse<EmployeePayrollDTO> updatePayroll(
            @PathVariable Long employeeId,
            @PathVariable Long payrollId,
            @Valid @RequestBody EmployeePayrollDTO payrollDTO) {
        return ApiResponse.ok(employeePayrollService.update(employeeId, payrollId, payrollDTO));
    }

    @DeleteMapping("/{employeeId}/payrolls/{payrollId}")
    public ApiResponse<Void> deletePayroll(
            @PathVariable Long employeeId,
            @PathVariable Long payrollId) {
        employeePayrollService.delete(employeeId, payrollId);
        return ApiResponse.ok(null);
    }
}