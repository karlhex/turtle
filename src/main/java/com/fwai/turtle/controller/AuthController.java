package com.fwai.turtle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.service.interfaces.AuthService;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/signin")
  ResponseEntity<SigninAns> signin(@RequestBody SigninReq signinReq) {
    return ResponseEntity.ok(authService.signin(signinReq));
  }

  @PostMapping("/signup")
  ResponseEntity<SigninAns> signup(@RequestBody SignupReq signupReq) {
    return ResponseEntity.ok(authService.signup(signupReq));
  }

}
