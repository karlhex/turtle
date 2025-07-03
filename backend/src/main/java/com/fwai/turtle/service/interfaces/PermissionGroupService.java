package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.PermissionGroupDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface PermissionGroupService {
    PermissionGroupDTO create(PermissionGroupDTO permissionGroupDTO);
    
    PermissionGroupDTO update(Long id, PermissionGroupDTO permissionGroupDTO);
    
    void delete(Long id);
    
    PermissionGroupDTO findById(Long id);
    
    Page<PermissionGroupDTO> findAll(Pageable pageable);
    
    Page<PermissionGroupDTO> findAllActive(Pageable pageable);
    
    Page<PermissionGroupDTO> search(String query, Pageable pageable);
    
    Set<PermissionGroupDTO> findByRoleIds(Set<Long> roleIds);
    
    void toggleActive(Long id);
    
    void addRolesToGroup(Long groupId, Set<Long> roleIds);
    
    void removeRolesFromGroup(Long groupId, Set<Long> roleIds);
    
    void addPermissionsToGroup(Long groupId, Set<Long> permissionIds);
    
    void removePermissionsFromGroup(Long groupId, Set<Long> permissionIds);
}
