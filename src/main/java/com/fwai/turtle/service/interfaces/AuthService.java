package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.dto.RefreshTokenRequest;

public interface AuthService {
  public abstract SigninAns signin(SigninReq signinReq);

  public SigninAns signup(SignupReq signinReq);
  
  public SigninAns refreshToken(RefreshTokenRequest refreshTokenRequest);
}
