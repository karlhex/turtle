package com.fwai.turtle.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {
    private String accessToken;  // 短期访问令牌
    private String refreshToken; // 长期刷新令牌
    private long accessTokenExpiry;
    private long refreshTokenExpiry;
}
