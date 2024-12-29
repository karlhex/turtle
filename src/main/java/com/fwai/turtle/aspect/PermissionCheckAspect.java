package com.fwai.turtle.aspect;

import com.fwai.turtle.annotation.RequirePermission;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.service.interfaces.RolePermissionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Aspect
@Component
public class PermissionCheckAspect {

    @Autowired
    private RolePermissionService rolePermissionService;

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("User not authenticated");
        }

        // Convert authorities to roles
        Set<Role> roles = authentication.getAuthorities().stream()
                .map(authority -> {
                    Role role = new Role();
                    role.setName(authority.getAuthority());
                    return role;
                })
                .collect(Collectors.toSet());

        String transactionPath = requirePermission.value();
        if (!rolePermissionService.hasPermission(roles, transactionPath)) {
            return ResponseEntity.status(403).body("User does not have permission to access: " + transactionPath);
        }

        // 如果权限检查通过，继续执行原方法
        return joinPoint.proceed();
    }
}
