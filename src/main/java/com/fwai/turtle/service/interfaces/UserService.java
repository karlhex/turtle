package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.persistence.model.User;
import java.util.Optional;

public interface UserService {
  public Optional<User> findByEmail(String email);

  public Optional<User> findById(Long id);

  public User newUser(User user);

  public User updateUser(User user);

  public User destroyUser(User user);

}
