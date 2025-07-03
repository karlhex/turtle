package com.fwai.turtle.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.RoleDTO;
import com.fwai.turtle.service.interfaces.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleDTO>> getAllRoles() {
        return ApiResponse.ok(roleService.getAllRoles());
    }

    @GetMapping("/system")
    public ApiResponse<List<RoleDTO>> getSystemRoles() {
        return ApiResponse.ok(roleService.getSystemRoles());
    }

    @GetMapping("/{name}")
    public ApiResponse<RoleDTO> getRoleByName(@PathVariable String name) {
        return ApiResponse.ok(roleService.getRoleByName(name));
    }

    @PostMapping
    public ApiResponse<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        return ApiResponse.ok(roleService.createRole(roleDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<RoleDTO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return ApiResponse.ok(roleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ApiResponse.ok(null);
    }
}
