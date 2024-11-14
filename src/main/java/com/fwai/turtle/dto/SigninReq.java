package com.fwai.turtle.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SigninReq {
  private String username;
  private String password;
}
