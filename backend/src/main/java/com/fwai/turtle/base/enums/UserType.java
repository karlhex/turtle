package com.fwai.turtle.base.enums;

public enum UserType {
    SYSTEM("系统用户"),
    EMPLOYEE("员工用户"), 
    GUEST("访客用户");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}