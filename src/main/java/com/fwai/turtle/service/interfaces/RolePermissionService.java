package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.RolePermissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;
import com.fwai.turtle.persistence.entity.Role;

public interface RolePermissionService {
    Page<RolePermissionDTO> findAll(Pageable pageable);
    RolePermissionDTO findById(Long id);
    RolePermissionDTO create(RolePermissionDTO rolePermissionDTO);
    RolePermissionDTO update(Long id, RolePermissionDTO rolePermissionDTO);
    void delete(Long id);
    List<RolePermissionDTO> findByRoleName(String roleName);

    boolean hasPermission(Set<Role> roles, String transactionPath); // Add this method

    Set<String> getPermittedPatterns(Set<Role> roles); // Add this method

    RolePermissionDTO toggleActive(Long id);
}
