package com.fwai.turtle.base.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fwai.turtle.base.dto.RoleDTO;
import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.mapper.RoleMapper;
import com.fwai.turtle.base.repository.RoleRepository;
import com.fwai.turtle.base.service.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleMapper.toDTOList(roleRepository.findAll());
    }

    @Override
    public List<RoleDTO> getSystemRoles() {
        return roleMapper.toDTOList(roleRepository.findByIsSystem(true));
    }

    @Override
    public RoleDTO getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found: " + name));
        return roleMapper.toDTO(role);
    }

    @Override
    @Transactional
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new RuntimeException("Role already exists: " + roleDTO.getName());
        }

        Role role = roleMapper.toEntity(roleDTO);
        role.setId(null); // Ensure we're creating a new role
        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        // Check if new name conflicts with existing role
        if (!role.getName().equals(roleDTO.getName()) && roleRepository.existsByName(roleDTO.getName())) {
            throw new RuntimeException("Role already exists: " + roleDTO.getName());
        }

        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());
        role.setIsSystem(roleDTO.getIsSystem());

        return roleMapper.toDTO(roleRepository.save(role));
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        
        if (role.getIsSystem()) {
            throw new RuntimeException("Cannot delete system role: " + role.getName());
        }

        roleRepository.delete(role);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}
