package com.fwai.turtle.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.types.RoleType;
import com.fwai.turtle.persistence.repository.RoleRepository;
import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.exception.DuplicateRecordException;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public User signup(SignupReq signupReq) {
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

    return userService.newUser(user);
  }
}
