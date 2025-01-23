package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.service.interfaces.PermissionCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Set;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionCheckController {
    private final PermissionCheckService permissionCheckService;

    @GetMapping("/check/{permission}")
    public ApiResponse<Boolean> hasPermission(@PathVariable String permission) {
        return ApiResponse.ok(permissionCheckService.hasPermission(permission));
    }

    @PostMapping("/check-batch")
    public ApiResponse<Boolean> hasAnyPermission(@RequestBody String[] permissions) {
        return ApiResponse.ok(permissionCheckService.hasAnyPermission(permissions));
    }

    @GetMapping("/current")
    public ApiResponse<Set<String>> getCurrentUserPermissions() {
        return ApiResponse.ok(permissionCheckService.getCurrentUserPermissions());
    }
}
