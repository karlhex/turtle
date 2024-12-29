package com.fwai.turtle.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.dto.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.persistence.mapper.UserMapper;
import com.fwai.turtle.persistence.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private UserMapper userMapper;

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
      log.info("Finding user by username: {}", username);
      Optional<User> userOpt = userRepository.findByUsername(username);
      
      if (userOpt.isPresent()) {
        User user = userOpt.get();
        log.info("Found user: {}", user);
        log.info("User roles: {}", user.getRoles());
        if (user.getRoles() != null) {
          user.getRoles().forEach(role -> {
            log.info("Role: {}, ID: {}, Name: {}", role, role.getId(), role.getName());
          });
        } else {
          log.warn("User roles is null");
        }
      } else {
        log.warn("User not found with username: {}", username);
      }
      
      return userOpt;
    } catch (Exception e) {
      log.error("Error finding user by username: {}", username, e);
      return Optional.empty();
    }
  }

  @Override
  public User newUser(User user) {
    try {
      log.info("Creating new user: {}", user);
      return userRepository.save(user);
    } catch (Exception e) {
      log.error("Error creating new user", e);
      throw e;
    }
  }

  @Override
  public User updateUser(UserDTO userDTO) {
    try {
      User oldUser = userRepository.findById(userDTO.getId())
          .orElseThrow(() -> new UsernameNotFoundException("User not found"));
      
      User user = userMapper.toEntity(userDTO);

      user.setPassword(oldUser.getPassword());
      log.info("Updating user: {}", user);
      return userRepository.save(user);
    } catch (Exception e) {
      log.error("Error updating user", e);
      throw e;
    }
  }

  @Override
  public Page<UserDTO> findAll(Pageable pageable) {
    try {
      Page<User> userPage = userRepository.findAll(pageable);
      log.info("Retrieved {} users", userPage.getTotalElements());
      return userPage.map(userMapper::toDTO);
    } catch (Exception e) {
      log.error("Error retrieving users", e);
      throw e;
    }
  }

  @Override
  public Page<UserDTO> findUnmappedUsers(Pageable pageable) {
    try {
      Page<User> users = userRepository.findUnmappedUsers(pageable);
      log.info("Retrieved {} unmapped users", users.getTotalElements());
      return users.map(userMapper::toDTO);
    } catch (Exception e) {
      log.error("Error retrieving unmapped users", e);
      throw e;
    }
  }

  @Override
  public Page<UserDTO> searchUsers(String query, Pageable pageable) {
    try {
      Page<User> userPage = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
          query, query, pageable);
      log.info("Found {} users matching query: {}", userPage.getTotalElements(), query);
      return userPage.map(userMapper::toDTO);
    } catch (Exception e) {
      log.error("Error searching users with query: {}", query, e);
      throw e;
    }
  }
}
