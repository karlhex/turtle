package com.fwai.turtle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.dto.RefreshTokenRequest;
import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.common.Result;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public Result<SigninAns> signin(@RequestBody SigninReq signinReq) {
    return Result.success(authService.signin(signinReq));
  }

  @PostMapping("/signup")
  public Result<SigninAns> signup(@RequestBody SignupReq signupReq) {
    return Result.success(authService.signup(signupReq));
  }

  @PostMapping("/refresh")
  public Result<SigninAns> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    return Result.success(authService.refreshToken(refreshTokenRequest));
  }
}
