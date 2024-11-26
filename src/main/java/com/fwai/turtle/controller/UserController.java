package com.fwai.turtle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.exception.ResourceNotFoundException;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ApiResponse<List<User>> getUsers() {
    log.info("getUsers");
    return ApiResponse.ok(userService.findAll());
  }

  @GetMapping("/{id}")
  public ApiResponse<User> getUser(@PathVariable("id") Long id) {
    log.info("getUser: {}", id);
    return ApiResponse.ok(userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
  }

  @GetMapping("/unmapped")
  public ApiResponse<List<User>> getUnmappedUsers() {
    log.info("getUnmappedUsers");
    return ApiResponse.ok(userService.findUnmappedUsers());
  }

  @PostMapping
  public ApiResponse<User> createUser(@RequestBody User user) {
    log.info("createUser: {}", user);
    return ApiResponse.ok(userService.newUser(user));
  }

  @DeleteMapping("/{id}")
  public ApiResponse<User> destroyUser(@PathVariable("id") Long id) {
    log.info("destroyUser: {}", id);
    User user = userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return ApiResponse.ok(userService.destroyUser(user));
  }

  @PutMapping("/{id}")
  public ApiResponse<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
    log.info("updateUser: {}", id);
    if (!userService.findById(id).isPresent()) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    user.setId(id);
    return ApiResponse.ok(userService.updateUser(user));
  }
}
