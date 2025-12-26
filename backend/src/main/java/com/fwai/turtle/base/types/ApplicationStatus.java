package com.fwai.turtle.base.types;

/**
 * 入职申请状态枚举
 */
public enum ApplicationStatus {
    PENDING("待审批"),
    VALIDATED("HR总监已审批"),
    APPROVED("已通过"),
    REJECTED("已拒绝"),
    UNDER_REVIEW("审核中");

    private final String description;

    ApplicationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}