package com.fwai.turtle.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.fwai.turtle.service.interfaces.UserService;
import com.fwai.turtle.dto.UserDTO;
import com.fwai.turtle.dto.ChangePasswordRequest;
import com.fwai.turtle.exception.InvalidCredentialsException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.Collections;
import java.util.Random;

import com.fwai.turtle.persistence.entity.User;
import com.fwai.turtle.persistence.mapper.UserMapper;
import com.fwai.turtle.persistence.repository.UserRepository;
import com.fwai.turtle.service.PasswordValidationService;
import com.fwai.turtle.service.interfaces.RolePermissionService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidationService passwordValidationService;
    private final RolePermissionService rolePermissionService;

    private static final String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final int TEMP_PASSWORD_LENGTH = 12;

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

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        // 1. 检查新密码和确认密码是否一致
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("新密码和确认密码不一致");
        }

        // 2. 获取当前登录用户
        User currentUser = userRepository.findByUsername(
            SecurityContextHolder.getContext().getAuthentication().getName()
        ).orElseThrow(() -> new UsernameNotFoundException("用户未找到"));

        // 3. 检查账户是否被锁定
        if (currentUser.isAccountLocked()) {
            if (currentUser.getAccountLockedUntil() != null && 
                currentUser.getAccountLockedUntil().isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("账户已被锁定，请稍后再试");
            } else {
                // 如果锁定时间已过，重置锁定状态
                currentUser.resetFailedLoginAttempts();
            }
        }

        // 4. 验证当前密码是否正确
        if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), currentUser.getPassword())) {
            throw new InvalidCredentialsException("当前密码不正确");
        }

        // 5. 验证新密码的强度
        List<String> validationErrors = passwordValidationService.validatePassword(changePasswordRequest.getNewPassword());
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException(String.join(", ", validationErrors));
        }

        // 6. 检查是否是常见密码
        if (passwordValidationService.isCommonPassword(changePasswordRequest.getNewPassword())) {
            throw new IllegalArgumentException("请勿使用常见密码");
        }

        // 7. 更新密码
        currentUser.updatePassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(currentUser);

        log.info("用户 {} 密码修改成功", currentUser.getUsername());
    }

    @Override
    public String resetPassword(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("用户未找到"));

        // 生成随机临时密码
        String tempPassword = generateSecurePassword();
        
        // 更新用户密码并设置为过期状态
        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setPasswordExpired(true);
        user.setPasswordUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        log.info("用户 {} 密码已重置", user.getUsername());
        
        return tempPassword;
    }

    @Override
    public boolean isPasswordExpired(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("用户未找到"));
        return user.isPasswordExpired();
    }

    @Override
    public void forcePasswordChange(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("用户未找到"));
        user.setPasswordExpired(true);
        userRepository.save(user);
        log.info("已强制用户 {} 下次登录时修改密码", user.getUsername());
    }

    @Override
    public void handleLoginFailure(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.incrementFailedLoginAttempts();
            userRepository.save(user);
            log.warn("用户 {} 登录失败，失败次数: {}", username, user.getFailedLoginAttempts());
        });
    }

    @Override
    public void resetLoginAttempts(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.resetFailedLoginAttempts();
            userRepository.save(user);
            log.info("用户 {} 登录失败次数已重置", username);
        });
    }

    @Override
    public boolean isAccountLocked(String username) {
        return userRepository.findByUsername(username)
            .map(user -> user.isAccountLocked() && 
                 user.getAccountLockedUntil() != null && 
                 user.getAccountLockedUntil().isAfter(LocalDateTime.now()))
            .orElse(false);
    }

    @Override
    public Set<String> getCurrentUserPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Collections.emptySet();
        }

        Optional<User> userOpt = findByUsername(authentication.getName());
        if (userOpt.isEmpty()) {
            return Collections.emptySet();
        }

        User user = userOpt.get();
        return rolePermissionService.getPermittedPatterns(user.getRoles());
    }

    private String generateSecurePassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        
        // 确保包含至少一个大写字母、小写字母、数字和特殊字符
        password.append(ALLOWED_CHARS.charAt(random.nextInt(26))); // 大写字母
        password.append(ALLOWED_CHARS.charAt(26 + random.nextInt(26))); // 小写字母
        password.append(ALLOWED_CHARS.charAt(52 + random.nextInt(10))); // 数字
        password.append(ALLOWED_CHARS.charAt(62 + random.nextInt(8))); // 特殊字符
        
        // 填充剩余长度
        while (password.length() < TEMP_PASSWORD_LENGTH) {
            password.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        
        // 打乱字符顺序
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }
        
        return new String(passwordArray);
    }
}
