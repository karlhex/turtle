package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.PermissionGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface PermissionGroupRepository extends JpaRepository<PermissionGroup, Long> {
    Optional<PermissionGroup> findByName(String name);
    
    boolean existsByName(String name);
    
    @Query("SELECT pg FROM PermissionGroup pg WHERE pg.active = true")
    Page<PermissionGroup> findAllActive(Pageable pageable);
    
    @Query("SELECT pg FROM PermissionGroup pg WHERE LOWER(pg.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(pg.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<PermissionGroup> search(@Param("query") String query, Pageable pageable);
    
    @Query("SELECT pg FROM PermissionGroup pg JOIN pg.roles r WHERE r.id IN :roleIds")
    Set<PermissionGroup> findByRoleIds(@Param("roleIds") Set<Long> roleIds);
}
