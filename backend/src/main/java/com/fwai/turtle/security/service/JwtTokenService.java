package com.fwai.turtle.security.service;

import com.fwai.turtle.security.dto.TokenPair;
import com.fwai.turtle.base.entity.Role;
import java.util.Set;

public interface JwtTokenService {
    TokenPair createTokenPair(String username, Set<Role> roles);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    boolean isTokenExpired(String token);
    TokenPair refreshTokenPair(String refreshToken);
    Set<Role> getRolesFromToken(String token);
    void revokeToken(String token);
    boolean isAccessToken(String token);
    boolean isRefreshToken(String token);
}
