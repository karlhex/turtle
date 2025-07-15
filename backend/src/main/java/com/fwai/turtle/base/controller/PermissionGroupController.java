package com.fwai.turtle.base.controller;

import com.fwai.turtle.base.annotation.RequirePermission;
import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.base.dto.PermissionGroupDTO;
import com.fwai.turtle.base.service.PermissionGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api/permission-groups")
@RequiredArgsConstructor
public class PermissionGroupController {
    private final PermissionGroupService permissionGroupService;

    @PostMapping
    @RequirePermission("system.permission.group.create")
    public ApiResponse<PermissionGroupDTO> create(@Valid @RequestBody PermissionGroupDTO permissionGroupDTO) {
        return ApiResponse.ok(permissionGroupService.create(permissionGroupDTO));
    }

    @PutMapping("/{id}")
    @RequirePermission("system.permission.group.update")
    public ApiResponse<PermissionGroupDTO> update(@PathVariable Long id, @Valid @RequestBody PermissionGroupDTO permissionGroupDTO) {
        return ApiResponse.ok(permissionGroupService.update(id, permissionGroupDTO));
    }

    @DeleteMapping("/{id}")
    @RequirePermission("system.permission.group.delete")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        permissionGroupService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}")
    @RequirePermission("system.permission.group.view")
    public ApiResponse<PermissionGroupDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(permissionGroupService.findById(id));
    }

    @GetMapping
    @RequirePermission("system.permission.group.view")
    public ApiResponse<Page<PermissionGroupDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {
        return ApiResponse.ok(permissionGroupService.findAll(
                PageRequest.of(page, size, Sort.by(direction, sortBy))));
    }

    @GetMapping("/active")
    @RequirePermission("system.permission.group.view")
    public ApiResponse<Page<PermissionGroupDTO>> getAllActive(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(permissionGroupService.findAllActive(PageRequest.of(page, size)));
    }

    @GetMapping("/search")
    @RequirePermission("system.permission.group.view")
    public ApiResponse<Page<PermissionGroupDTO>> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ApiResponse.ok(permissionGroupService.search(query, PageRequest.of(page, size)));
    }

    @PutMapping("/{id}/toggle-active")
    @RequirePermission("system.permission.group.update")
    public ApiResponse<Void> toggleActive(@PathVariable Long id) {
        permissionGroupService.toggleActive(id);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/roles")
    @RequirePermission("system.permission.group.update")
    public ApiResponse<Void> addRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        permissionGroupService.addRolesToGroup(id, roleIds);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}/roles")
    @RequirePermission("system.permission.group.update")
    public ApiResponse<Void> removeRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        permissionGroupService.removeRolesFromGroup(id, roleIds);
        return ApiResponse.ok(null);
    }

    @PostMapping("/{id}/permissions")
    @RequirePermission("system.permission.group.update")
    public ApiResponse<Void> addPermissions(@PathVariable Long id, @RequestBody Set<Long> permissionIds) {
        permissionGroupService.addPermissionsToGroup(id, permissionIds);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}/permissions")
    @RequirePermission("system.permission.group.update")
    public ApiResponse<Void> removePermissions(@PathVariable Long id, @RequestBody Set<Long> permissionIds) {
        permissionGroupService.removePermissionsFromGroup(id, permissionIds);
        return ApiResponse.ok(null);
    }
}
