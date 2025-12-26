package com.fwai.turtle.base.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import com.fwai.turtle.base.enums.PermissionType;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Table(name = "role_permissions")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class RolePermission extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "transaction_pattern", nullable = false)
    private String transactionPattern;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_type", nullable = false)
    @Builder.Default
    private PermissionType permissionType = PermissionType.SPECIFIC;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

}
