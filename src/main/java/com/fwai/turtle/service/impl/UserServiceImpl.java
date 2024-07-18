package com.fwai.turtle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fwai.turtle.service.interfaces.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import com.fwai.turtle.persistence.model.User;
import com.fwai.turtle.persistence.repositories.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;

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
    return null;
  }

  @Override
  public Optional<User> findByEmail(String email) {
    try {
      return userRepository.findByEmail(email);
    } catch (Exception e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public User newUser(User user) {
    try {
      return userRepository.save(user);
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public User updateUser(User user) {
    return null;
  }
}
