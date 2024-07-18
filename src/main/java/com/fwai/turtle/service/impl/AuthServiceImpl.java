package com.fwai.turtle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.fwai.turtle.api.dto.SigninAns;
import com.fwai.turtle.api.dto.SigninReq;
import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import com.fwai.turtle.service.interfaces.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.fwai.turtle.persistence.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import com.fwai.turtle.api.dto.SignupReq;

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

    if (signinReq.getEmail().equals("") && signinReq.getPassword().equals("")) {
      throw new IllegalArgumentException("用户名不能为空");
    }

    final User user = userService.findByEmail(signinReq.getEmail())
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    final Authentication authentication = authenticationProvider
        .authenticate(new UsernamePasswordAuthenticationToken(signinReq.getEmail(), signinReq.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    return new SigninAns(user.getId(),
        jwtTokenService.createToken(user.getUsername(), user.getRoles()),
        user.getFirstName(),
        user.getLastName());
  }

  @Override
  public SigninAns signup(SignupReq signupReq) {
    log.info("signup " + signupReq.toString());

    var user = User.builder().firstName(signupReq.getFirstName()).lastName(signupReq.getLastName())
        .email(signupReq.getEmail())
        .username(signupReq.getUsername())
        .password(passwordEncoder.encode(signupReq.getPassword()))
        .build();

    user = userService.newUser(user);
    return new SigninAns(user.getId(),
        jwtTokenService.createToken(user.getEmail(), user.getRoles()),
        user.getFirstName(),
        user.getLastName());
  }

}
