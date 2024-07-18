package com.fwai.turtle.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SigninAns {
  private long id;
  private String token;
  private String firstName;
  private String lastName;
}
