package com.fwai.turtle.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.api.dto.SigninAns;
import com.fwai.turtle.api.dto.SigninReq;
import com.fwai.turtle.api.dto.SignupReq;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  ResponseEntity<SigninAns> signin(SigninReq signinReq) {
    return ResponseEntity.ok(authService.signin(signinReq));
  }

  @PostMapping("/signup")
  ResponseEntity<SigninAns> signup(SignupReq signupReq) {
    return ResponseEntity.ok(authService.signup(signupReq));
  }

}
