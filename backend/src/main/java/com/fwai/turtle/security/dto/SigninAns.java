package com.fwai.turtle.security.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class SigninAns {
  private Long id;
  private TokenPair tokenPair;
  private Long employeeId;
  private String employeeName;
  private String employeeDepartment;
  private String employeePosition;
  private boolean isSystemUser;
  private Set<String> permissions;
  private String token;
}
