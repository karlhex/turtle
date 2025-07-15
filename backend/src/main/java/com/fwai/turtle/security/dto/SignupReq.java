package com.fwai.turtle.security.dto;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupReq {
    String firstName;
    String lastName;

    String email;
    String password;
    String username;

    Set<String> roles;
}
