package com.fwai.turtle.service.interfaces;

import java.util.List;

import com.fwai.turtle.dto.RoleDTO;

public interface RoleService {
    List<RoleDTO> getAllRoles();
    List<RoleDTO> getSystemRoles();
    RoleDTO getRoleByName(String name);
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
    boolean existsByName(String name);
}
