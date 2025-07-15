package com.fwai.turtle.base.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.base.dto.RolePermissionDTO;
import com.fwai.turtle.base.service.RolePermissionService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {
    private final RolePermissionService rolePermissionService;

    @GetMapping
    public ApiResponse<Page<RolePermissionDTO>> findAll(Pageable pageable) {
        return ApiResponse.ok(rolePermissionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<RolePermissionDTO> findById(@PathVariable Long id) {
        return ApiResponse.ok(rolePermissionService.findById(id));
    }

    @PostMapping
    public ApiResponse<RolePermissionDTO> create(@RequestBody RolePermissionDTO rolePermissionDTO) {
        return ApiResponse.ok(rolePermissionService.create(rolePermissionDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<RolePermissionDTO> update(@PathVariable Long id, @RequestBody RolePermissionDTO rolePermissionDTO) {
        return ApiResponse.ok(rolePermissionService.update(id, rolePermissionDTO));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        rolePermissionService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/role/{roleName}")
    public ApiResponse<List<RolePermissionDTO>> findByRoleName(@PathVariable String roleName) {
        return ApiResponse.ok(rolePermissionService.findByRoleName(roleName));
    }

    @PutMapping("/{id}/toggle-active")
    public ApiResponse<RolePermissionDTO> toggleActive(@PathVariable Long id) {
        return ApiResponse.ok(rolePermissionService.toggleActive(id));
    }
}
