package com.fwai.turtle.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fwai.turtle.types.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Set<RoleType> roleNames;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
