package com.fwai.turtle.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;

  public UserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

    log.debug(user.toString());

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), 
        user.getPassword(),
        getAuthority(user.getRoles())
    );
  }

  private List<SimpleGrantedAuthority> getAuthority(Set<Role> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }
}
