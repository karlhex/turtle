package com.fwai.turtle.service;

import com.fwai.turtle.dto.RolePermissionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RolePermissionService {
    Page<RolePermissionDTO> findAll(Pageable pageable);
    RolePermissionDTO findById(Long id);
    RolePermissionDTO create(RolePermissionDTO rolePermissionDTO);
    RolePermissionDTO update(Long id, RolePermissionDTO rolePermissionDTO);
    void delete(Long id);
    List<RolePermissionDTO> findByRoleName(String roleName);
}
