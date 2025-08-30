package com.fwai.turtle.base.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fwai.turtle.modules.organization.entity.Employee;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = {"employee", "roles","password"})
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "password_expired", nullable = false)
    @Builder.Default
    private boolean passwordExpired = true;

    @Column(name = "password_updated_at")
    private LocalDateTime passwordUpdatedAt;

    @Column(name = "password_expiry_days")
    @Builder.Default
    private Integer passwordExpiryDays = 90;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "account_locked")
    @Builder.Default
    private boolean accountLocked = false;

    @Column(name = "account_locked_until")
    private LocalDateTime accountLockedUntil;

    @Column(unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user","educations", "attendances", "leaves", "jobHistories", "department", "position"})
    private Employee employee;    // 关联员工信息

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            this.accountLocked = true;
            this.accountLockedUntil = LocalDateTime.now().plusMinutes(30);
        }
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.accountLockedUntil = null;
    }

    public boolean isPasswordExpired() {
        if (this.passwordExpired) {
            return true;
        }
        
        if (this.passwordUpdatedAt == null || this.passwordExpiryDays == null) {
            return false;
        }

        LocalDateTime expiryDate = this.passwordUpdatedAt.plusDays(this.passwordExpiryDays);
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
        this.passwordExpired = false;
        this.passwordUpdatedAt = LocalDateTime.now();
    }
}
