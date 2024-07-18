package com.fwai.turtle.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import com.fwai.turtle.persistence.model.User;
import com.fwai.turtle.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/{id}")
  public User getUser(@PathVariable("id") Long id) {
    log.info("getUser: {}", id);
    return userService.findById(id).orElse(null);
  }
}
