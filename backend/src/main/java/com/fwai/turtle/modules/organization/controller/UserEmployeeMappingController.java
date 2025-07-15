package com.fwai.turtle.modules.organization.controller;

import com.fwai.turtle.modules.organization.dto.UserEmployeeMappingDTO;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.modules.organization.service.UserEmployeeMappingService;
import com.fwai.turtle.base.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-employee-mappings")
@RequiredArgsConstructor
public class UserEmployeeMappingController {
    private final UserEmployeeMappingService mappingService;

    @GetMapping
    public ApiResponse<List<UserEmployeeMappingDTO>> getAllMappings() {
        return ApiResponse.ok(mappingService.getAllMappings());
    }

    @PostMapping
    public ApiResponse<Void> createMapping(@RequestParam Long userId, @RequestParam Long employeeId) {
        mappingService.createMapping(userId, employeeId);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{employeeId}")
    public ApiResponse<Void> deleteMapping(@PathVariable Long employeeId) {
        mappingService.deleteMapping(employeeId);
        return ApiResponse.ok(null);
    }

    @GetMapping("/unmapped-users")
    public ApiResponse<List<User>> getUnmappedUsers() {
        return ApiResponse.ok(mappingService.getUnmappedUsers());
    }

    @GetMapping("/unmapped-employees")
    public ApiResponse<List<Employee>> getUnmappedEmployees() {
        return ApiResponse.ok(mappingService.getUnmappedEmployees());
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<UserEmployeeMappingDTO> getMappingByUserId(@PathVariable Long userId) {
        UserEmployeeMappingDTO mapping = mappingService.getMappingByUserId(userId);
        return ApiResponse.ok(mapping);
    }
}
