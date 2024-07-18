package com.fwai.turtle.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SigninReq {
  private String email;
  private String password;
}
