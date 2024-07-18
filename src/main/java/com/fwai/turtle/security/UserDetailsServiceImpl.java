package com.fwai.turtle.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fwai.turtle.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;

import com.fwai.turtle.persistence.model.User;
import com.fwai.turtle.persistence.model.Role;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserService userService;

  public UserDetails loadUserByUsername(String username) {

    log.info("loadUserByUsername " + username);

    User user = userService.findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + "not found"));

    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
        getAuthority(user.getRoles()));
  }

  private List<SimpleGrantedAuthority> getAuthority(Set<Role> roles) {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName().name())).toList();
  }
}
