package com.fwai.turtle.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.types.RoleType;
import com.fwai.turtle.persistence.repository.RoleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fwai.turtle.dto.SigninAns;
import com.fwai.turtle.dto.SigninReq;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.dto.RefreshTokenRequest;
import com.fwai.turtle.persistence.entity.Role;
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

  @Autowired
  private RoleRepository roleRepository;

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
          jwtTokenService.createToken(user.getUsername(), user.getRoles()));
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

    Set<Role> roles = new HashSet<>();
    for (String roleStr : signupReq.getRoles()) {
      try {
        RoleType roleType = RoleType.valueOf(roleStr.toUpperCase());
        roleRepository.findByName(roleType)
            .ifPresent(roles::add);
      } catch (IllegalArgumentException e) {
        log.warn("Invalid role type: " + roleStr);
      }
    }

    // If no valid roles provided or found, assign default USER role
    if (roles.isEmpty()) {
      roleRepository.findByName(RoleType.USER)
          .ifPresent(roles::add);
    }

    var user = User.builder()
        .email(signupReq.getEmail())
        .username(signupReq.getUsername())
        .password(passwordEncoder.encode(signupReq.getPassword()))
        .roles(roles)
        .build();

    user = userService.newUser(user);
    return new SigninAns(user.getId(),
        jwtTokenService.createToken(user.getEmail(), user.getRoles()));
  }

  @Override
  public SigninAns refreshToken(RefreshTokenRequest refreshTokenRequest) {
    log.info("refreshToken for token: {}", refreshTokenRequest.getToken());
    
    if (refreshTokenRequest.getToken() == null || refreshTokenRequest.getToken().isEmpty()) {
      throw new AuthenticationException("Token cannot be empty");
    }

    try {
      // 验证当前token是否有效
      if (!jwtTokenService.validateToken(refreshTokenRequest.getToken())) {
        throw new AuthenticationException("Invalid or expired token");
      }

      // 从token中获取用户名
      String username = jwtTokenService.getUsernameFromToken(refreshTokenRequest.getToken());
      
      // 获取用户信息
      User user = userService.findByUsername(username)
          .orElseThrow(() -> new AuthenticationException("User not found"));

      // 生成新的token
      String newToken = jwtTokenService.refreshToken(refreshTokenRequest.getToken());
      
      return new SigninAns(user.getId(), newToken);
    } catch (Exception e) {
      log.error("Error refreshing token", e);
      throw new AuthenticationException("Failed to refresh token: " + e.getMessage());
    }
  }
}
