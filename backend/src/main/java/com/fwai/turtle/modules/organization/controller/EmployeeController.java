package com.fwai.turtle.modules.organization.controller;

import com.fwai.turtle.base.annotation.RequirePermission;
import com.fwai.turtle.base.dto.ApiResponse;
import org.springframework.data.domain.Page;
import com.fwai.turtle.modules.organization.dto.EmployeeDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeEducationDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.modules.organization.dto.EmployeeLeaveDTO;
import com.fwai.turtle.modules.organization.dto.EmployeePayrollDTO;
import com.fwai.turtle.modules.organization.service.EmployeeService;
import com.fwai.turtle.modules.organization.service.impl.EmployeeAttendanceServiceImpl;
import com.fwai.turtle.modules.organization.service.EmployeeLeaveService;
import com.fwai.turtle.modules.organization.service.EmployeePayrollService;
import com.fwai.turtle.modules.organization.service.EmployeeEducationService;
import com.fwai.turtle.modules.organization.service.EmployeeJobHistoryService;
import com.fwai.turtle.base.types.EmployeeStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeEducationService employeeEducationService;
    private final EmployeeAttendanceServiceImpl employeeAttendanceService;
    private final EmployeeLeaveService employeeLeaveService;
    private final EmployeePayrollService employeePayrollService;
    private final EmployeeJobHistoryService employeeJobHistoryService;

    @PostMapping
    @RequirePermission("hr.employee.create")
    public ApiResponse<EmployeeDTO> create(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return ApiResponse.ok(employeeService.create(employeeDTO));
    }

    @PutMapping("/{id}")
    @RequirePermission("hr.employee.update")
    public ApiResponse<EmployeeDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ApiResponse.ok(employeeService.update(id, employeeDTO));
    }

    @GetMapping("/{id}")
    @RequirePermission("hr.employee.view")
    public ApiResponse<EmployeeDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(employeeService.getById(id));
    }

    @GetMapping
    @RequirePermission("hr.employee.view")
    public ApiResponse<Page<EmployeeDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        return ApiResponse.ok(employeeService.getAll(page, size, sortBy, direction));
    }

    @GetMapping("/search")
    @RequirePermission("hr.employee.view")
    public ApiResponse<Page<EmployeeDTO>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(employeeService.search(query, page, size));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("hr.employee.delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/unmapped")
    @RequirePermission("hr.employee.view")
    public ApiResponse<List<EmployeeDTO>> getUnmappedEmployees() {
        return ApiResponse.ok(employeeService.getUnmappedEmployees());
    }

    @GetMapping("/active")
    @RequirePermission("hr.employee.view")
    public ApiResponse<List<EmployeeDTO>> getActiveEmployees() {
        return ApiResponse.ok(employeeService.getActiveEmployees());
    }

    @GetMapping("/unassigned")
    @RequirePermission("hr.employee.view")
    public ApiResponse<List<EmployeeDTO>> getUnassignedEmployees() {
        return ApiResponse.ok(employeeService.getUnassignedEmployees());
    }

    @GetMapping("/status/{status}")
    @RequirePermission("hr.employee.view")
    public ApiResponse<List<EmployeeDTO>> getEmployeesByStatus(@PathVariable String status) {
        return ApiResponse.ok(employeeService.getEmployeesByStatus(EmployeeStatus.valueOf(status)));
    }

    @PostMapping("/{employeeId}/educations")
    @RequirePermission("hr.employee.education.create")
    public ApiResponse<EmployeeEducationDTO> addEducation(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeEducationDTO educationDTO) {
        return ApiResponse.ok(employeeEducationService.add(employeeId, educationDTO));
    }
    
    @PutMapping("/{employeeId}/educations/{educationId}")
    @RequirePermission("hr.employee.education.update")
    public ApiResponse<EmployeeEducationDTO> updateEducation(
            @PathVariable Long employeeId,
            @PathVariable Long educationId,
            @Valid @RequestBody EmployeeEducationDTO educationDTO) {
        return ApiResponse.ok(employeeEducationService.update(employeeId, educationId, educationDTO));
    }
    
    @DeleteMapping("/{employeeId}/educations/{educationId}")
    @RequirePermission("hr.employee.education.delete")
    public ApiResponse<Void> deleteEducation(
            @PathVariable Long employeeId,
            @PathVariable Long educationId) {
        employeeEducationService.delete(employeeId, educationId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{employeeId}/educations")
    @RequirePermission("hr.employee.education.view")
    public ApiResponse<List<EmployeeEducationDTO>> getEducations(@PathVariable Long employeeId) {
        return ApiResponse.ok(employeeEducationService.getByEmployeeId(employeeId));
    }

    @PostMapping("/{employeeId}/attendance")
    @RequirePermission("hr.employee.attendance.create")
    public ApiResponse<EmployeeAttendanceDTO> addAttendance(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeAttendanceDTO attendanceDTO) {
        return ApiResponse.ok(employeeAttendanceService.add(employeeId, attendanceDTO));
    }

    @PutMapping("/{employeeId}/attendance/{attendanceId}")
    @RequirePermission("hr.employee.attendance.update")
    public ApiResponse<EmployeeAttendanceDTO> updateAttendance(
            @PathVariable Long employeeId,
            @PathVariable Long attendanceId,
            @Valid @RequestBody EmployeeAttendanceDTO attendanceDTO) {
        return ApiResponse.ok(employeeAttendanceService.update(employeeId, attendanceId, attendanceDTO));
    }

    @GetMapping("/{employeeId}/attendance")
    @RequirePermission("hr.employee.attendance.view")
    public ApiResponse<List<EmployeeAttendanceDTO>> getAttendance(@PathVariable Long employeeId) {
        return ApiResponse.ok(employeeAttendanceService.getByEmployeeId(employeeId));
    }

    @DeleteMapping("/{employeeId}/attendance/{attendanceId}")
    @RequirePermission("hr.employee.attendance.delete")
    public ApiResponse<Void> deleteAttendance(
            @PathVariable Long employeeId,
            @PathVariable Long attendanceId) {
        employeeAttendanceService.delete(employeeId, attendanceId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{employeeId}/job-history")
    @RequirePermission("hr.employee.job.view")
    public ApiResponse<List<EmployeeJobHistoryDTO>> getJobHistory(@PathVariable Long employeeId) {
        return ApiResponse.ok(employeeJobHistoryService.getByEmployeeId(employeeId));
    }

    @PostMapping("/{employeeId}/job-history")
    @RequirePermission("hr.employee.job.create")
    public ApiResponse<EmployeeJobHistoryDTO> createJobHistory(@PathVariable Long employeeId, @RequestBody EmployeeJobHistoryDTO jobHistoryDTO) {
        jobHistoryDTO.setEmployeeId(employeeId);
        return ApiResponse.ok(employeeJobHistoryService.add(employeeId, jobHistoryDTO));
    }

    @PutMapping("/{employeeId}/job-history/{id}")
    @RequirePermission("hr.employee.job.update")
    public ApiResponse<EmployeeJobHistoryDTO> updateJobHistory(@PathVariable Long employeeId, @PathVariable Long id, @RequestBody EmployeeJobHistoryDTO jobHistoryDTO) {
        jobHistoryDTO.setEmployeeId(employeeId);
        return ApiResponse.ok(employeeJobHistoryService.update(employeeId, id, jobHistoryDTO));
    }

    @DeleteMapping("/{employeeId}/job-history/{id}")
    @RequirePermission("hr.employee.job.delete")
    public ApiResponse<Void> deleteJobHistory(@PathVariable Long employeeId, @PathVariable Long id) {
        employeeJobHistoryService.delete(employeeId, id);
        return ApiResponse.ok(null);
    }

    // Employee Leave APIs
    @PostMapping("/{employeeId}/leaves")
    @RequirePermission("hr.employee.leave.create")
    public ApiResponse<EmployeeLeaveDTO> addLeave(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeLeaveDTO leaveDTO) {
        return ApiResponse.ok(employeeLeaveService.add(employeeId, leaveDTO));
    }

    @PutMapping("/{employeeId}/leaves/{leaveId}")
    @RequirePermission("hr.employee.leave.update")
    public ApiResponse<EmployeeLeaveDTO> updateLeave(
            @PathVariable Long employeeId,
            @PathVariable Long leaveId,
            @Valid @RequestBody EmployeeLeaveDTO leaveDTO) {
        return ApiResponse.ok(employeeLeaveService.update(employeeId, leaveId, leaveDTO));
    }

    @DeleteMapping("/{employeeId}/leaves/{leaveId}")
    @RequirePermission("hr.employee.leave.delete")
    public ApiResponse<Void> deleteLeave(
            @PathVariable Long employeeId,
            @PathVariable Long leaveId) {
        employeeLeaveService.delete(employeeId, leaveId);
        return ApiResponse.ok(null);
    }

    // Employee Payroll APIs
    @PostMapping("/{employeeId}/payrolls")
    @RequirePermission("hr.employee.payroll.create")
    public ApiResponse<EmployeePayrollDTO> addPayroll(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeePayrollDTO payrollDTO) {
        return ApiResponse.ok(employeePayrollService.add(employeeId, payrollDTO));
    }

    @PutMapping("/{employeeId}/payrolls/{payrollId}")
    @RequirePermission("hr.employee.payroll.update")
    public ApiResponse<EmployeePayrollDTO> updatePayroll(
            @PathVariable Long employeeId,
            @PathVariable Long payrollId,
            @Valid @RequestBody EmployeePayrollDTO payrollDTO) {
        return ApiResponse.ok(employeePayrollService.update(employeeId, payrollId, payrollDTO));
    }

    @DeleteMapping("/{employeeId}/payrolls/{payrollId}")
    @RequirePermission("hr.employee.payroll.delete")
    public ApiResponse<Void> deletePayroll(
            @PathVariable Long employeeId,
            @PathVariable Long payrollId) {
        employeePayrollService.delete(employeeId, payrollId);
        return ApiResponse.ok(null);
    }
}