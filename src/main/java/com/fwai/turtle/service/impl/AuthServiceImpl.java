package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.*;
import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.persistence.repository.RoleRepository;
import com.fwai.turtle.service.interfaces.AuthService;
import com.fwai.turtle.service.interfaces.JwtTokenService;
import com.fwai.turtle.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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
    @Transactional
    public SigninAns signin(SigninReq signinReq) {
        // 验证用户名密码
        authenticationProvider.authenticate(
            new UsernamePasswordAuthenticationToken(signinReq.getUsername(), signinReq.getPassword())
        );

        Optional<User> userOpt = userService.findByUsername(signinReq.getUsername());
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        TokenPair tokenPair = jwtTokenService.createTokenPair(user.getUsername(), user.getRoles());

        return SigninAns.builder()
                .id(user.getId())
                .tokenPair(tokenPair)
                .employeeId(user.getEmployee().getId())
                .employeeName(user.getEmployee().getName())
                .employeeDepartment(user.getEmployee().getDepartment().getName())
                .employeePosition(user.getEmployee().getPosition())
                .isSystemUser(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_SYSTEM_USER")))
                .build();
    }

    @Override
    @Transactional
    public SigninAns signup(SignupReq signupReq) {
        // 检查用户名是否已存在
        if (userService.findByUsername(signupReq.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(signupReq.getUsername());
        user.setPassword(passwordEncoder.encode(signupReq.getPassword()));
        user.setEmail(signupReq.getEmail());

        // 设置默认角色
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        roles.add(userRole);
        user.setRoles(roles);

        // 保存用户
        user = userService.newUser(user);

        // 生成令牌
        TokenPair tokenPair = jwtTokenService.createTokenPair(user.getUsername(), user.getRoles());

        return SigninAns.builder()
                .id(user.getId())
                .tokenPair(tokenPair)
                .build();
    }

    @Override
    public SigninAns refreshToken(RefreshTokenRequest refreshTokenRequest) {
        TokenPair tokenPair = jwtTokenService.refreshTokenPair(refreshTokenRequest.getToken());
        
        String username = jwtTokenService.getUsernameFromToken(tokenPair.getAccessToken());
        Optional<User> userOpt = userService.findByUsername(username);
        if (!userOpt.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();

        return SigninAns.builder()
                .id(user.getId())
                .tokenPair(tokenPair)
                .employeeId(user.getEmployee().getId())
                .employeeName(user.getEmployee().getName())
                .employeeDepartment(user.getEmployee().getDepartment().getName())
                .employeePosition(user.getEmployee().getPosition())
                .isSystemUser(user.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_SYSTEM_USER")))
                .build();
    }

    @Override
    public void logout(String accessToken) {
        if (!jwtTokenService.isAccessToken(accessToken)) {
            throw new IllegalArgumentException("Invalid access token");
        }
        jwtTokenService.revokeToken(accessToken);
    }

    @Override
    public void logoutAll(String username) {
        // 这里可以实现逻辑来撤销用户的所有活跃令牌
        // 可以通过查询TokenState表来获取所有活跃的令牌
        // 然后调用revokeToken来撤销它们
    }
}
