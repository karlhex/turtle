package com.fwai.turtle.base.repository;

import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleAndIsActiveTrue(Role role);
    
    @Query("SELECT rp FROM RolePermission rp JOIN rp.role r WHERE r.name IN :roleNames AND rp.isActive = true")
    List<RolePermission> findByRolesAndActive(@Param("roleNames") Set<String> roleNames);
}
