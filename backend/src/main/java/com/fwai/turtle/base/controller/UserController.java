package com.fwai.turtle.base.controller;

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

import com.fwai.turtle.base.entity.User;
import com.fwai.turtle.base.service.UserService;
import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.security.dto.UserDTO;
import com.fwai.turtle.security.dto.UserCreationResult;
import com.fwai.turtle.security.dto.ChangePasswordRequest;
import com.fwai.turtle.security.dto.ExpiredPasswordChangeRequest;
import com.fwai.turtle.base.exception.ResourceNotFoundException;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

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
  public ApiResponse<UserCreationResult> createUser(@Valid @RequestBody UserDTO userDTO) {
    log.info("createUser - incoming UserDTO: {}", userDTO);
    log.info("createUser - userType: {}, roleNames: {}", userDTO.getUserType(), userDTO.getRoleNames());
    UserCreationResult result = userService.createUserWithTempPassword(userDTO);
    log.info("createUser - created User: {}", result.getUser());
    log.info("createUser - created User userType: {}", result.getUser().getUserType());
    log.info("createUser - temporary password generated successfully");
    return ApiResponse.ok(result);
  }

  @DeleteMapping("/{id}")
  public ApiResponse<User> destroyUser(@PathVariable("id") Long id) {
    log.info("destroyUser: {}", id);
    User user = userService.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    return ApiResponse.ok(userService.destroyUser(user));
  }

  @PutMapping("/{id}")
  public ApiResponse<User> updateUser(@PathVariable("id") Long id, @RequestBody UserDTO userDTO) {
    log.info("updateUser: {}", id);
    if (!userService.findById(id).isPresent()) {
      throw new ResourceNotFoundException("User not found with id: " + id);
    }
    userDTO.setId(id);
    return ApiResponse.ok(userService.updateUser(userDTO));
  }

  @PostMapping("/change-password")
  public ApiResponse<String> changePassword(@RequestBody Map<String, Object> requestMap) {
    try {
      // 验证必要字段
      if (!requestMap.containsKey("currentPassword") || !requestMap.containsKey("newPassword") || !requestMap.containsKey("confirmPassword")) {
        throw new IllegalArgumentException("缺少必要的密码字段");
      }
      
      if (requestMap.containsKey("username")) {
        // 包含用户名，说明是过期密码修改请求
        ExpiredPasswordChangeRequest expiredPasswordChangeRequest = new ExpiredPasswordChangeRequest();
        expiredPasswordChangeRequest.setUsername((String) requestMap.get("username"));
        expiredPasswordChangeRequest.setCurrentPassword((String) requestMap.get("currentPassword"));
        expiredPasswordChangeRequest.setNewPassword((String) requestMap.get("newPassword"));
        expiredPasswordChangeRequest.setConfirmPassword((String) requestMap.get("confirmPassword"));
        
        log.info("Changing expired password for user: {}", expiredPasswordChangeRequest.getUsername());
        userService.changeExpiredPassword(expiredPasswordChangeRequest);
        return ApiResponse.ok("密码修改成功");
      } else {
        // 不包含用户名，说明是普通密码修改请求
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword((String) requestMap.get("currentPassword"));
        changePasswordRequest.setNewPassword((String) requestMap.get("newPassword"));
        changePasswordRequest.setConfirmPassword((String) requestMap.get("confirmPassword"));
        
        log.info("Changing password for current user");
        userService.changePassword(changePasswordRequest);
        return ApiResponse.ok("密码修改成功");
      }
    } catch (Exception e) {
      log.error("Error changing password", e);
      throw e;
    }
  }
}
