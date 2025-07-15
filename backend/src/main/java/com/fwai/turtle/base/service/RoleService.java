package com.fwai.turtle.base.service;

import java.util.List;

import com.fwai.turtle.base.dto.RoleDTO;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    List<RoleDTO> getSystemRoles();
    RoleDTO getRoleByName(String name);
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
    boolean existsByName(String name);
}
