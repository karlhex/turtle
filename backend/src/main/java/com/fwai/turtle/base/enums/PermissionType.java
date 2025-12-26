package com.fwai.turtle.base.enums;

public enum PermissionType {
    SPECIFIC("具体权限"),
    ALL("全部权限");

    private final String description;

    PermissionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}