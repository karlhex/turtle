package com.fwai.turtle.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.InternalAuthenticationServiceException;

import com.fwai.turtle.security.dto.SigninAns;
import com.fwai.turtle.security.dto.SigninReq;
import com.fwai.turtle.security.dto.SignupReq;
import com.fwai.turtle.security.dto.RefreshTokenRequest;
import com.fwai.turtle.security.service.AuthService;
import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.security.dto.LogoutReq;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  public ApiResponse<SigninAns> signin(@RequestBody SigninReq signinReq) {
    try {
      return ApiResponse.ok(authService.signin(signinReq));
    } catch (InternalAuthenticationServiceException ex) {
      if (ex.getMessage() != null && ex.getMessage().contains("Password has expired")) {
        return ApiResponse.error(40301, "密码已过期，请修改密码");
      }
      throw ex; // Re-throw if it's not a password expiry issue
    }
  }

  @PostMapping("/signup")
  public ApiResponse<SigninAns> signup(@RequestBody SignupReq signupReq) {
    return ApiResponse.ok(authService.signup(signupReq));
  }

  @PostMapping("/refresh")
  public ApiResponse<SigninAns> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
    return ApiResponse.ok(authService.refreshToken(refreshTokenRequest));
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(@RequestBody LogoutReq logoutReq) {
    authService.logout(logoutReq);
    return ApiResponse.ok(null);
  }

  @PostMapping("/logout-all")
  public ApiResponse<Void> logoutAll(@RequestBody String username) {
    authService.logoutAll(username);
    return ApiResponse.ok(null);
  }
}
