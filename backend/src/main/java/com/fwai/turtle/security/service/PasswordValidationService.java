package com.fwai.turtle.security.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PasswordValidationService {
    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 32;
    private static final Pattern HAS_UPPER = Pattern.compile("[A-Z]");
    private static final Pattern HAS_LOWER = Pattern.compile("[a-z]");
    private static final Pattern HAS_NUMBER = Pattern.compile("\\d");
    private static final Pattern HAS_SPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    
    public List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();
        
        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            errors.add("密码长度必须在" + MIN_LENGTH + "到" + MAX_LENGTH + "个字符之间");
        }
        
        if (!HAS_UPPER.matcher(password).find()) {
            errors.add("密码必须包含至少一个大写字母");
        }
        
        if (!HAS_LOWER.matcher(password).find()) {
            errors.add("密码必须包含至少一个小写字母");
        }
        
        if (!HAS_NUMBER.matcher(password).find()) {
            errors.add("密码必须包含至少一个数字");
        }
        
        if (!HAS_SPECIAL.matcher(password).find()) {
            errors.add("密码必须包含至少一个特殊字符(!@#$%^&*(),.?\":{}|<>)");
        }
        
        return errors;
    }
    
    public boolean isValidPassword(String password) {
        return validatePassword(password).isEmpty();
    }
    
    public boolean isCommonPassword(String password) {
        // 这里可以添加常见密码的检查,比如"password123","admin123"等
        String[] commonPasswords = {
            "password123", "admin123", "12345678", "qwerty123",
            "letmein123", "welcome123", "monkey123", "football123"
        };
        
        String lowerPassword = password.toLowerCase();
        for (String commonPassword : commonPasswords) {
            if (lowerPassword.equals(commonPassword)) {
                return true;
            }
        }
        
        return false;
    }
}
