package com.fwai.turtle.base.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fwai.turtle.security.dto.ChangePasswordRequest;
import com.fwai.turtle.security.dto.UserDTO;
import com.fwai.turtle.base.entity.User;

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
    
    /**
     * 修改用户密码
     * @param changePasswordRequest 密码修改请求
     * @throws IllegalArgumentException 如果密码不符合要求
     * @throws IllegalStateException 如果用户账户被锁定
     */
    void changePassword(ChangePasswordRequest changePasswordRequest);
    
    /**
     * 重置用户密码
     * @param userId 用户ID
     * @return 新生成的随机密码
     */
    String resetPassword(Long userId);
    
    /**
     * 检查用户密码是否过期
     * @param userId 用户ID
     * @return true如果密码已过期
     */
    boolean isPasswordExpired(Long userId);
    
    /**
     * 强制用户下次登录时修改密码
     * @param userId 用户ID
     */
    void forcePasswordChange(Long userId);
    
    /**
     * 处理登录失败
     * @param username 用户名
     */
    void handleLoginFailure(String username);
    
    /**
     * 重置登录失败次数
     * @param username 用户名
     */
    void resetLoginAttempts(String username);
    
    /**
     * 检查账户是否被锁定
     * @param username 用户名
     * @return true如果账户被锁定
     */
    boolean isAccountLocked(String username);

    /**
     * Get the permissions of the currently authenticated user
     * @return Set of permission strings for the current user
     */
    Set<String> getCurrentUserPermissions();
}
