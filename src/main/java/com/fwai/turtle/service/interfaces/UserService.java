package com.fwai.turtle.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fwai.turtle.dto.ChangePasswordRequest;
import com.fwai.turtle.dto.UserDTO;
import com.fwai.turtle.persistence.entity.User;

public interface UserService {
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User newUser(User user);
    User updateUser(UserDTO user);
    User destroyUser(User user);
    Page<UserDTO> findAll(Pageable pageable);
    Page<UserDTO> findUnmappedUsers(Pageable pageable);
    Page<UserDTO> searchUsers(String query, Pageable pageable);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
