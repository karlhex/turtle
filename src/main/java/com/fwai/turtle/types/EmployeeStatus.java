package com.fwai.turtle.types;

/**
 * 员工状态枚举
 */
public enum EmployeeStatus {
    /**
     * 申请中
     */
    APPLICATION("申请"),

    /**
     * 在职
     */
    ACTIVE("在职"),

    /**
     * 离职
     */
    RESIGNED("离职"),

    /**
     * 暂停
     */
    SUSPENDED("暂停");

    private final String description;

    EmployeeStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
