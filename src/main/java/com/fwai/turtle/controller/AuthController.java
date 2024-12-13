package com.fwai.turtle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.persistence.entity.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signup")
  public ApiResponse<User> signup(@RequestBody SignupReq signupReq) {
    return ApiResponse.ok(authService.signup(signupReq));
  }
}
