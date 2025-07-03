package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.TokenPair;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.service.TokenAuditService;
import com.fwai.turtle.service.TokenBlacklistService;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtTokenServiceImpl implements JwtTokenService {
    @Value("${token.signing.issuer}")
    private String jwtIssuer;

    @Value("${token.signing.secretKey}")
    private String secretKey;

    @Value("${token.access.expiration:900}") // 默认15分钟
    private long accessTokenExpiration;

    @Value("${token.refresh.expiration:604800}") // 默认7天
    private long refreshTokenExpiration;

    private final TokenBlacklistService blacklistService;
    private final TokenAuditService auditService;

    public JwtTokenServiceImpl(TokenBlacklistService blacklistService, TokenAuditService auditService) {
        this.blacklistService = blacklistService;
        this.auditService = auditService;
    }

    @Override
    public TokenPair createTokenPair(String username, Set<Role> roles) {
        String tokenId = UUID.randomUUID().toString();
        String deviceId = UUID.randomUUID().toString();

        String accessToken = createToken(username, roles, "ACCESS", tokenId, deviceId, accessTokenExpiration);
        String refreshToken = createToken(username, roles, "REFRESH", tokenId, deviceId, refreshTokenExpiration);

        auditService.logTokenEvent(username, tokenId, "TOKEN_CREATED");

        return new TokenPair(
            accessToken,
            refreshToken,
            System.currentTimeMillis() + accessTokenExpiration * 1000,
            System.currentTimeMillis() + refreshTokenExpiration * 1000
        );
    }

    private String createToken(String username, Set<Role> roles, String tokenType, 
                             String tokenId, String deviceId, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles.stream().map(Role::getName).collect(Collectors.toList()));
        claims.put("tokenType", tokenType);
        claims.put("tokenId", tokenId);
        claims.put("deviceId", deviceId);

        return Jwts.builder()
            .claims(claims)
            .subject(username)
            .issuer(jwtIssuer)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(getSecretKey())
            .compact();
    }

    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean validateToken(String token) {
        if (blacklistService.isTokenBlacklisted(token)) {
            return false;
        }
        try {
            Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public TokenPair refreshTokenPair(String refreshToken) {
        if (!isRefreshToken(refreshToken) || !validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = getUsernameFromToken(refreshToken);
        Set<Role> roles = getRolesFromToken(refreshToken);
        String oldTokenId = extractClaim(refreshToken, claims -> claims.get("tokenId", String.class));

        // 创建新的令牌对
        TokenPair newTokenPair = createTokenPair(username, roles);

        // 将旧令牌加入黑名单
        revokeToken(refreshToken);
        
        auditService.logTokenEvent(username, oldTokenId, "TOKEN_REFRESHED");

        return newTokenPair;
    }

    @Override
    public Set<Role> getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        @SuppressWarnings("unchecked")
        List<String> roleNames = (List<String>) claims.get("roles", List.class);
        return roleNames.stream()
            .map(name -> {
                Role role = new Role();
                role.setName(name);
                return role;
            })
            .collect(Collectors.toSet());
    }

    @Override
    public void revokeToken(String token) {
        if (!validateToken(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        String username = getUsernameFromToken(token);
        String tokenId = extractClaim(token, claims -> claims.get("tokenId", String.class));
        Date expiration = extractExpiration(token);
        long expirationSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;

        blacklistService.blacklistToken(token, expirationSeconds);
        auditService.logTokenEvent(username, tokenId, "TOKEN_REVOKED");
    }

    @Override
    public boolean isAccessToken(String token) {
        return "ACCESS".equals(extractClaim(token, claims -> claims.get("tokenType", String.class)));
    }

    @Override
    public boolean isRefreshToken(String token) {
        return "REFRESH".equals(extractClaim(token, claims -> claims.get("tokenType", String.class)));
    }

    private Date extractExpiration(String token) throws ExpiredJwtException {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws ExpiredJwtException {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
