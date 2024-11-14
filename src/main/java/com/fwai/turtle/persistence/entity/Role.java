package com.fwai.turtle.persistence.entity;

import java.util.Set;

import com.fwai.turtle.types.RoleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", unique = true)
    private RoleType name;

    @Column(name = "role_description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(RoleType name) {
        this.name = name;
    }
}
