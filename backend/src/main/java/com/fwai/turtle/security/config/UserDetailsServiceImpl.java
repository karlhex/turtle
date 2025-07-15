package com.fwai.turtle.security.config;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.CredentialsExpiredException;

import com.fwai.turtle.base.entity.Role;
import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.service.UserService;
import com.fwai.turtle.base.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserService userService;
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserService userService, UserRepository userRepository) {
    this.userService = userService;
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userService.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(username + " not found"));

    log.debug("Loading user: {} with locked status: {}, password expired: {}", 
              username, user.isAccountLocked(), user.isPasswordExpired());

    // Check if account is locked
    if (user.isAccountLocked()) {
      if (user.getAccountLockedUntil() != null && user.getAccountLockedUntil().isAfter(java.time.LocalDateTime.now())) {
        throw new LockedException("Account is locked until " + user.getAccountLockedUntil());
      } else {
        // Unlock account if lock period has expired
        user.setAccountLocked(false);
        user.setAccountLockedUntil(null);
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
      }
    }

    // Check if password is expired
    if (user.isPasswordExpired()) {
      throw new CredentialsExpiredException("Password has expired");
    }

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(), 
        user.getPassword(),
        true,  // enabled
        true,  // accountNonExpired
        !user.isPasswordExpired(),  // credentialsNonExpired
        !user.isAccountLocked(),    // accountNonLocked
        getAuthority(user.getRoles())
    );
  }

  private List<SimpleGrantedAuthority> getAuthority(Set<Role> roles) {
    return roles.stream()
        .map(role -> new SimpleGrantedAuthority(role.getName()))
        .collect(Collectors.toList());
  }
}
