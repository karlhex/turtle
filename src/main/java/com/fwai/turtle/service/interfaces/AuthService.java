package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.dto.RefreshTokenRequest;
import com.fwai.turtle.dto.LogoutReq;

public interface AuthService {
    SigninAns signin(SigninReq signinReq);
    SigninAns signup(SignupReq signinReq);
    SigninAns refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(LogoutReq logoutReq);
    void logoutAll(String username);
}
