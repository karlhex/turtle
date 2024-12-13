package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.SignupReq;
import com.fwai.turtle.persistence.entity.User;

public interface AuthService {
  User signup(SignupReq signupReq);
}
