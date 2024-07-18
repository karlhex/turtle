package com.fwai.turtle.persistence.types;

public enum RoleType {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private String value;

  RoleType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
