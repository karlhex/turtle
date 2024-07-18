package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.api.dto.SigninAns;
import com.fwai.turtle.api.dto.SigninReq;
import com.fwai.turtle.api.dto.SignupReq;

public interface AuthService {
  public abstract SigninAns signin(SigninReq signinReq);

  public SigninAns signup(SignupReq signinReq);
}
