package com.fwai.turtle.service.impl;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import com.fwai.turtle.service.interfaces.UserService;
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
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.types.EmployeeStatus;

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

      log.info("Found user: {}", user);

      // Authenticate user credentials
      final Authentication authentication = authenticationProvider
          .authenticate(new UsernamePasswordAuthenticationToken(signinReq.getUsername(), signinReq.getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      // Initialize response builder
      SigninAns.SigninAnsBuilder ansBuilder = SigninAns.builder()
          .id(user.getId());

      Set<Role> roles = new HashSet<>(user.getRoles());

      // Check if user is SYSTEM
      if (roles.stream().anyMatch(role -> role.getIsSystem())) {
        String token = jwtTokenService.createToken(user.getUsername(), roles);
        return ansBuilder
            .isSystemUser(true) 
            .token(token)
            .roles(roles.stream().map(role -> role.getName().replace("ROLE_", "")).collect(Collectors.toSet()))
            .build();
      }

      // Handle employee cases
      Employee employee = user.getEmployee();
      if (employee == null) {
        // If employee is empty, assign guest role
        Role guestRole = roleRepository.findByName("ROLE_GUEST")
            .orElseThrow(() -> new RuntimeException("Guest role not found"));
        roles.clear();
        roles.add(guestRole);
        String token = jwtTokenService.createToken(user.getUsername(), roles);
        return ansBuilder
            .isSystemUser(false)  
            .token(token)
            .roles(roles.stream().map(role -> role.getName().replace("ROLE_", "")).collect(Collectors.toSet()))
            .build();
      }

      // Check employee status
      if (employee.getStatus() == EmployeeStatus.RESIGNED || employee.getStatus() == EmployeeStatus.SUSPENDED) {
        throw new AuthenticationException("员工状态不正常，不能登录");
      }

      // If status is APPLICATION, assign GUEST role
      if (employee.getStatus() == EmployeeStatus.APPLICATION) {
        Role guestRole = roleRepository.findByName("ROLE_GUEST")
            .orElseThrow(() -> new RuntimeException("Guest role not found"));
        roles.clear();
        roles.add(guestRole);
      }

      // Generate token and build response with employee info
      String token = jwtTokenService.createToken(user.getUsername(), roles);
      return ansBuilder
          .isSystemUser(false)
          .token(token)
          .roles(roles.stream().map(role -> role.getName().replace("ROLE_", "")).collect(Collectors.toSet()))
          .employeeId(employee.getId())
          .employeeName(employee.getName())
          .employeeDepartment(employee.getDepartment() != null ? employee.getDepartment().getName() : null)
          .employeePosition(employee.getPosition())
          .build();

    } catch (BadCredentialsException e) {
      log.error("Authentication failed for user: {}", signinReq.getUsername(), e);
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
        roleRepository.findByName(roleStr)
            .ifPresent(roles::add);
      } catch (IllegalArgumentException e) {
        log.warn("Invalid role type: " + roleStr);
      }
    }

    // If no valid roles provided or found, assign default USER role
    if (roles.isEmpty()) {
      roleRepository.findByName("ROLE_GUEST")
          .ifPresent(roles::add);
    }

    var user = User.builder()
        .email(signupReq.getEmail())
        .username(signupReq.getUsername())
        .password(passwordEncoder.encode(signupReq.getPassword()))
        .roles(roles)
        .build();

    user = userService.newUser(user);
    return SigninAns.builder()
        .id(user.getId())
        .token(jwtTokenService.createToken(user.getEmail(), user.getRoles()))
        .build();
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
      
      return SigninAns.builder()
          .id(user.getId())
          .token(newToken)
          .build();
    } catch (Exception e) {
      log.error("Error refreshing token", e);
      throw new AuthenticationException("Failed to refresh token: " + e.getMessage());
    }
  }
}
