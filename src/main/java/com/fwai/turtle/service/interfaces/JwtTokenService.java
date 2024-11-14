package com.fwai.turtle.service.interfaces;

import java.util.Set;

import com.fwai.turtle.persistence.entity.Role;

public interface JwtTokenService {
    String createToken(String username, Set<Role> roles);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    boolean isTokenExpired(String token);
    String refreshToken(String token);

    Set<Role> getRolesFromToken(String token);    

}
