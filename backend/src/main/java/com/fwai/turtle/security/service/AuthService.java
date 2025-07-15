package com.fwai.turtle.security.service;

import com.fwai.turtle.security.dto.SigninAns;
import com.fwai.turtle.security.dto.SigninReq;
import com.fwai.turtle.security.dto.SignupReq;
import com.fwai.turtle.security.dto.RefreshTokenRequest;
import com.fwai.turtle.security.dto.LogoutReq;

public interface AuthService {
    SigninAns signin(SigninReq signinReq);
    SigninAns signup(SignupReq signinReq);
    SigninAns refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(LogoutReq logoutReq);
    void logoutAll(String username);
}
