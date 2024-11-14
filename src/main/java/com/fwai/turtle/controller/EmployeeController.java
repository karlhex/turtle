package com.fwai.turtle.controller;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.EmployeeDTO;
import com.fwai.turtle.dto.EmployeeEducationDTO;
import com.fwai.turtle.dto.EmployeeAttendanceDTO;
import com.fwai.turtle.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fwai.turtle.service.EmployeeEducationService;

import jakarta.validation.Valid;
import java.util.List;
import com.fwai.turtle.service.EmployeeAttendanceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final EmployeeEducationService employeeEducationService;
    private final EmployeeAttendanceService employeeAttendanceService;

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
    public ResponseEntity<Result<List<EmployeeDTO>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(employeeService.getAll(page, size));
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

} 