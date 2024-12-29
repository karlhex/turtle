package com.fwai.turtle.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.service.interfaces.JwtTokenService;

import javax.crypto.SecretKey;
import java.util.function.Function;
import java.util.HashSet;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Set;

// implement JwtTokenService 
@Slf4j
@NoArgsConstructor
@Component
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value("${token.signing.issuer}")
    private String jwtIssuer;

    @Value("${token.signing.secretKey}")
    private String secretKey;

    @Value("${token.signing.expiration}")
    private long jwtExpiration;

    public String createToken(String username, Set<Role> roles) {
        log.info(" user {} secretKey {} expiration {} roles {}", username, secretKey, jwtExpiration, roles);

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        long currentTimeMillis = System.currentTimeMillis();
        Date issuedAt = new Date(currentTimeMillis);
        Date expiration = new Date(currentTimeMillis + jwtExpiration * 60 * 24);

        // Convert roles to a list of role names
        Set<String> roleNames = roles.stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.toSet());

        try {
            return Jwts.builder()
                    .subject(username)
                    .claim("roles", String.join(",", roleNames))
                    .issuedAt(issuedAt)
                    .expiration(expiration)
                    .issuer(jwtIssuer)
                    .signWith(getSecetKey()).compact();
        } catch (Exception e) {
            log.error("Failed to create token", e);
            throw new RuntimeException("token生成失败");
        }
    }

    /**
     * 从TOKEN中获取用户名
     * 
     * @param token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * 判断token是否过期
     * 
     * @param token
     * @return true：过期
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * 刷新token
     * 
     * @param token
     * @return 新的token
     */
    public String refreshToken(String token) {
        String username = getUsernameFromToken(token);
        Set<Role> roles = getRolesFromToken(token);
        return createToken(username, roles);
    }

    /**
     * 从token中获取角色
     * 
     * @param token
     * @return 角色集合
     */
    public Set<Role> getRolesFromToken(String token) {
        final Claims claims = extrClaims(token);
        String roleString = claims.get("roles", String.class);
        String[] roles = roleString.split(",");
        Set<Role> rolesSet = new HashSet<Role>();

        for (int i = 0; i < roles.length; i++) {

            rolesSet.add(new Role(roles[i].trim()));
        }

        return rolesSet;
    }

    /**
     * 验证token
     * 
     * @param token
     * @return true：验证通过
     */
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    /**
     * 从token中获取过期时间
     * 
     * @param token
     * @return 过期时间
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * 获取用于加密或验证的密钥。
     * 
     * 此方法从预定义的密钥字符串中解码出密钥字节数组，并基于这些字节生成一个HMAC-SHA密钥。
     * HMAC-SHA是一种消息认证码算法，用于验证数据的完整性和真实性。
     * 
     * @return SecretKey 对象，代表生成的HMAC-SHA密钥。
     */
    private SecretKey getSecetKey() {
        // 使用BASE64解码密钥字符串，得到原始的密钥字节数组。
        byte[] secretKeyBytes = Decoders.BASE64.decode(secretKey);

        // 使用字节数组生成一个HMAC-SHA密钥，并返回。
        return Keys.hmacShaKeyFor(secretKeyBytes);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extrClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * 从JWT令牌中提取额外的声明。
     * 这个方法解析JWT令牌，并返回其中的载荷部分，载荷中通常包含用户信息和其他自定义声明。
     *
     * @param token JWT令牌，包含待提取的声明。
     * @return 解析后的Claims对象，其中包含JWT的载荷部分。
     */
    private Claims extrClaims(String token) {
        // 使用JJWT库的parser方法开始解析JWT令牌
        return Jwts
                // 配置解析器使用预定义的秘钥进行验证
                .parser()
                .verifyWith(getSecetKey())
                .build()
                // 使用解析器解析给定的JWT令牌并获取载荷部分
                .parseSignedClaims(token)
                .getPayload();
    }
}
