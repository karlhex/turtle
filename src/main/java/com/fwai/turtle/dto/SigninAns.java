package com.fwai.turtle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
