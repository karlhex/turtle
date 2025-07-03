package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.RolePermissionDTO;
import com.fwai.turtle.persistence.entity.RolePermission;
import com.fwai.turtle.persistence.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RolePermissionMapperTest {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Test
    void testToDTO() {
        Role role = new Role();
        role.setName("ADMIN");
        
        RolePermission entity = new RolePermission();
        entity.setRole(role);
        entity.setTransactionPattern("CREATE_USER");
        entity.setDescription("Create user permission");

        RolePermissionDTO dto = rolePermissionMapper.toDTO(entity);

        assertNotNull(dto);
        assertEquals("ADMIN", dto.getRoleName());
        assertEquals("CREATE_USER", dto.getTransactionPattern());
        assertEquals("Create user permission", dto.getDescription());
    }

    @Test
    void testToEntity() {
        RolePermissionDTO dto = new RolePermissionDTO();
        dto.setTransactionPattern("DELETE_USER");
        dto.setDescription("Delete user permission");

        RolePermission entity = rolePermissionMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("DELETE_USER", entity.getTransactionPattern());
        assertEquals("Delete user permission", entity.getDescription());
        assertTrue(entity.getIsActive());
    }
}
