package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.dto.RefreshTokenRequest;

public interface AuthService {
    SigninAns signin(SigninReq signinReq);
    SigninAns signup(SignupReq signinReq);
    SigninAns refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(String accessToken);
    void logoutAll(String username);
}
