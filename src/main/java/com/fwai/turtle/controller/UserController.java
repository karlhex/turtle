package com.fwai.turtle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.UserDTO;
import com.fwai.turtle.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping
  public ApiResponse<Page<UserDTO>> getUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String sortBy,
      @RequestParam(required = false) String direction) {
    log.info("getUsers - page: {}, size: {}, sortBy: {}, direction: {}", page, size, sortBy, direction);
    
    Pageable pageable;
    if (sortBy != null && direction != null) {
      Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
      pageable = PageRequest.of(page, size, sort);
    } else {
      pageable = PageRequest.of(page, size);
    }
    
    return ApiResponse.ok(userService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ApiResponse<User> getUser(@PathVariable("id") Long id) {
    log.info("getUser: {}", id);
    return ApiResponse.ok(userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)));
  }

  @GetMapping("/unmapped")
  public ApiResponse<Page<UserDTO>> getUnmappedUsers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    log.info("getUnmappedUsers - page: {}, size: {}", page, size);
    return ApiResponse.ok(userService.findUnmappedUsers(PageRequest.of(page, size)));
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
