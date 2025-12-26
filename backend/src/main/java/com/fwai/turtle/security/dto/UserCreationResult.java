package com.fwai.turtle.security.dto;

import com.fwai.turtle.base.entity.User;

/**
 * DTO for user creation result containing both user data and temporary password
 */
public class UserCreationResult {
    private User user;
    private String tempPassword;

    public UserCreationResult() {
    }

    public UserCreationResult(User user, String tempPassword) {
        this.user = user;
        this.tempPassword = tempPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    @Override
    public String toString() {
        return "UserCreationResult{" +
                "user=" + user +
                ", tempPassword='[HIDDEN]'" +
                '}';
    }
}