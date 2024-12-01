package com.fwai.turtle.persistence.entity;

public enum ContractStatus {
    PENDING_SIGNATURE("等待签署"),
    IN_PROGRESS("执行中"),
    COMPLETED("已结束"),
    CANCELLED("已取消"),
    SUSPENDED("已挂起");

    private final String description;

    ContractStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
