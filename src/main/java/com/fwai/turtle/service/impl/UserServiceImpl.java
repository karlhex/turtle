package com.fwai.turtle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fwai.turtle.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import com.fwai.turtle.persistence.entity.Role;
import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.persistence.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

  @Override
  public Optional<User> findByEmail(String email) {
    try {
      User user = userRepository.findByEmail(email).orElse(null);
      log.info("findByEmail: {}, user: {}", email, user);
      return Optional.ofNullable(user);
    } catch (Exception e) {
      log.error("Error finding user by email: {}", email, e);
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> findById(Long id) {
    try {
      User user = userRepository.findById(id).orElse(null);
      log.info("user " + user.toString());

      return Optional.ofNullable(user);
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public User destroyUser(User user) {
    try {
      userRepository.delete(user);
      return user;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public Optional<User> findByUsername(String username) {
    try {
      return userRepository.findByUsername(username);
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public User newUser(User user) {
    try {
      log.info("new user " + user.toString());
      for (Role role : user.getRoles()) {
        log.info("role " + role);
      }
      return userRepository.save(user);
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public User updateUser(User user) {
    try {
      User oldUser = userRepository.findById(user.getId())
          .orElseThrow(() -> new UsernameNotFoundException("user not found"));
      user.setPassword(oldUser.getPassword());
      return userRepository.save(user);
    } catch (Exception e) {
      throw e;
    }
  }
}
