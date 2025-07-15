package com.fwai.turtle.base.types;

public enum LeaveType {
    ANNUAL("年假"),
    SICK("病假"),
    PERSONAL("事假"),
    MARRIAGE("婚假"),
    MATERNITY("产假"),
    BEREAVEMENT("丧假"),
    OTHER("其他");
    
    private final String description;

    LeaveType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static LeaveType get(String name) {
        for (LeaveType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 