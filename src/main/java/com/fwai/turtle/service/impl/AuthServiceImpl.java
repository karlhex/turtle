package com.fwai.turtle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import com.fwai.turtle.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.exception.AuthenticationException;
import com.fwai.turtle.exception.DuplicateRecordException;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private AuthenticationProvider authenticationProvider;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public SigninAns signin(SigninReq signinReq) {
    log.info("signin " + signinReq.toString());

    if (signinReq.getUsername().isEmpty() || signinReq.getPassword().isEmpty()) {
      throw new AuthenticationException("Username and password cannot be empty");
    }

    try {
      final User user = userService.findByUsername(signinReq.getUsername())
          .orElseThrow(() -> new AuthenticationException("User not found"));

      final Authentication authentication = authenticationProvider
          .authenticate(new UsernamePasswordAuthenticationToken(signinReq.getUsername(), signinReq.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      return new SigninAns(user.getId(),
          jwtTokenService.createToken(user.getEmail(), user.getRoles()));
    } catch (BadCredentialsException e) {
      throw new AuthenticationException("Invalid username or password");
    }
  }

  @Override
  public SigninAns signup(SignupReq signupReq) {
    log.info("signup " + signupReq.toString());

    if (userService.findByUsername(signupReq.getUsername()).isPresent()) {
      throw new DuplicateRecordException("Username already exists");
    }

    if (userService.findByEmail(signupReq.getEmail()).isPresent()) {
      throw new DuplicateRecordException("Email already exists");
    }

    var user = User.builder()
        .email(signupReq.getEmail())
        .username(signupReq.getUsername())
        .password(passwordEncoder.encode(signupReq.getPassword()))
        .roles(signupReq.getRoles())
        .build();

    user = userService.newUser(user);
    return new SigninAns(user.getId(),
        jwtTokenService.createToken(user.getEmail(), user.getRoles()));
  }

}
