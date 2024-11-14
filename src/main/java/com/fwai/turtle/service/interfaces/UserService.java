package com.fwai.turtle.service.interfaces;

import java.util.Optional;

import com.fwai.turtle.persistence.entity.User;

public interface UserService {
  public Optional<User> findByUsername(String username);

  public Optional<User> findById(Long id);

  public User newUser(User user);

  public User updateUser(User user);

  public User destroyUser(User user);

}
