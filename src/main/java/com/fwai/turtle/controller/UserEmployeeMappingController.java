package com.fwai.turtle.controller;

import com.fwai.turtle.dto.UserEmployeeMappingDTO;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.service.UserEmployeeMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-employee-mappings")
@RequiredArgsConstructor
public class UserEmployeeMappingController {
    private final UserEmployeeMappingService mappingService;

    @GetMapping
    public ResponseEntity<List<UserEmployeeMappingDTO>> getAllMappings() {
        return ResponseEntity.ok(mappingService.getAllMappings());
    }

    @PostMapping
    public ResponseEntity<Void> createMapping(@RequestParam Long userId, @RequestParam Long employeeId) {
        mappingService.createMapping(userId, employeeId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteMapping(@PathVariable Long employeeId) {
        mappingService.deleteMapping(employeeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unmapped-users")
    public ResponseEntity<List<User>> getUnmappedUsers() {
        return ResponseEntity.ok(mappingService.getUnmappedUsers());
    }

    @GetMapping("/unmapped-employees")
    public ResponseEntity<List<Employee>> getUnmappedEmployees() {
        return ResponseEntity.ok(mappingService.getUnmappedEmployees());
    }
}
