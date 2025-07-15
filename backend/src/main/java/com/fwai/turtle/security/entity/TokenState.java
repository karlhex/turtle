package com.fwai.turtle.security.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class TokenState {
    @Id
    private String tokenId;
    
    private String username;
    
    @Enumerated(EnumType.STRING)
    private TokenStatus status;
    
    private LocalDateTime lastUsed;
    private String replacedBy;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
