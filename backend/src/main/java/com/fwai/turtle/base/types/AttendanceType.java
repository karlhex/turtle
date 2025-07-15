package com.fwai.turtle.base.types;

/**
 * 考勤类型枚举
 */
public enum AttendanceType {
    NORMAL("正常上班"),
    OVERTIME("加班"),
    BUSINESS_TRIP("出差"),
    WORK_FROM_HOME("远程办公"),
    LEAVE("请假");
    
    private final String description;

    AttendanceType(String description) {
        this.description = description;
    }

        // 请假

    public String getDescription() {
        return description;
    }

    public static AttendanceType get(String name) {
        for (AttendanceType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}