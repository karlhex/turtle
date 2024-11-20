package com.fwai.turtle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.common.Result;
import com.fwai.turtle.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/{id}")
  public Result<User> getUser(@PathVariable("id") Long id) {
    log.info("getUser: {}", id);
    return Result.success(userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
  }

  @DeleteMapping("/{id}")
  public Result<User> destroyUser(@PathVariable("id") Long id) {
    log.info("destroyUser: {}", id);
    User user = userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return Result.success(userService.destroyUser(user));
  }

  @PutMapping("/{id}")
  public Result<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
    log.info("updateUser: {}", id);
    if (!userService.findById(id).isPresent()) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    user.setId(id);
    return Result.success(userService.updateUser(user));
  }
}
