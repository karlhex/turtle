package com.fwai.turtle.base.types;

/**
 * 员工状态枚举
 */
public enum EmployeeStatus {
    APPLICATION("申请"),
    ACTIVE("在职"),
    RESIGNED("离职"),
    SUSPENDED("暂停");

    private final String description;

    EmployeeStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
