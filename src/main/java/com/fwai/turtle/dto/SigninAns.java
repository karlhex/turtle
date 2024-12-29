package com.fwai.turtle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SigninAns {
  private long id;
  private String token;
  private Long employeeId;
  private String employeeName;
  private String employeeDepartment;
  private String employeePosition;
  private Boolean isSystemUser;
  private Set<String> roles;
}
