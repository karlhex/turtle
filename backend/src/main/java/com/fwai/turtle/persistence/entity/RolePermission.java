package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
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

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
