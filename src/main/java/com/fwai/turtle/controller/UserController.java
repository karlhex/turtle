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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/{id}")
  public User getUser(@PathVariable("id") Long id) {
    log.info("getUser: {}", id);
    return userService.findById(id).orElse(null);
  }

  @DeleteMapping("/{id}")
  public User destroyUser(@PathVariable("id") Long id) {
    log.info("destroyUser: {}", id);
    return userService.destroyUser(userService.findById(id).orElse(null));
  }

  @PutMapping("/{id}")
  public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
    log.info("updateUser: {}", id);
    user.setId(id);
    return userService.updateUser(user);
  }
}
