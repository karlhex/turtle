package com.fwai.turtle.api.dto;

import java.util.Set;
import com.fwai.turtle.persistence.model.Role;

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

    Set<Role> roles;
}
