package com.fwai.turtle.base.types;

/**
 * 考勤状态枚举
 */
public enum AttendanceStatus {
    PENDING("待审核"),
    APPROVED("已审核"),
    REJECTED("已拒绝"),
    ABNORMAL("异常");
    
    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AttendanceStatus get(String name) {
        for (AttendanceStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 
