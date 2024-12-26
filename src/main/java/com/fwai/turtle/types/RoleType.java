package com.fwai.turtle.types;

public enum RoleType {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN"),
  SYSTEM("ROLE_SYSTEM"),
  GUEST("ROLE_GUEST");

  private String value;

  RoleType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
