package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.persistence.entity.Role;
import java.util.Set;

public interface RolePermissionService {
    boolean hasPermission(Set<Role> roles, String transactionPath);
    Set<String> getPermittedPatterns(Set<Role> roles);
}
