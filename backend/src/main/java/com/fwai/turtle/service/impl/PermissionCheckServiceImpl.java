package com.fwai.turtle.service.impl;

import com.fwai.turtle.service.interfaces.PermissionCheckService;
import com.fwai.turtle.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionCheckServiceImpl implements PermissionCheckService {
    private final UserService userService;

    @Override
    public boolean hasPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Set<String> userPermissions = userService.getCurrentUserPermissions();
        return userPermissions.contains(permission);
    }

    @Override
    public boolean hasAnyPermission(String[] permissions) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Set<String> userPermissions = userService.getCurrentUserPermissions();
        return Arrays.stream(permissions).anyMatch(userPermissions::contains);
    }

    // get current user all permissions
    @Override
    public Set<String> getCurrentUserPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptySet();
        }
        return userService.getCurrentUserPermissions();
    }
}
