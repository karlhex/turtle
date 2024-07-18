package com.fwai.turtle.service.interfaces;

import java.util.Set;

// JwtTokenService
// Author: Karl Hex
// Time: 2024-07-07

import com.fwai.turtle.persistence.model.Role;

public interface JwtTokenService {
    String createToken(String username, Set<Role> roles);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    boolean isTokenExpired(String token);
    String refreshToken(String token);

    Set<Role> getRolesFromToken(String token);    

}
