package com.fwai.turtle.base.types;

public enum ApprovalStatus {
    DRAFT("草稿"),
    PENDING("待审批"),
    APPROVED("已批准"),
    REJECTED("已拒绝"),
    CANCELLED("已取消"),
    WITHDRAWN("已撤回");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ApprovalStatus get(String name) {
        for (ApprovalStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}