package com.fwai.turtle.base.service;

import java.util.Set;

public interface PermissionCheckService {
    /**
     * Check if the current user has the specified permission
     *
     * @param permission the permission to check
     * @return true if the user has the permission, false otherwise
     */
    boolean hasPermission(String permission);

    /**
     * Check if the current user has any of the specified permissions
     *
     * @param permissions array of permissions to check
     * @return true if the user has any of the permissions, false otherwise
     */
    boolean hasAnyPermission(String[] permissions);

    /**
     * Get the permissions of the currently authenticated user
     * @return Set of permission strings for the current user
     */
    Set<String> getCurrentUserPermissions();
}
