package com.fwai.turtle.base.aspect;

import com.fwai.turtle.base.annotation.RequirePermission;
import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.service.RolePermissionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
            return ApiResponse.error(HttpStatus.UNAUTHORIZED.value(), "用户未认证");
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
            return ApiResponse.error(HttpStatus.FORBIDDEN.value(), "没有权限访问: " + transactionPath);
        }

        // 如果权限检查通过，继续执行原方法
        return joinPoint.proceed();
    }
}
