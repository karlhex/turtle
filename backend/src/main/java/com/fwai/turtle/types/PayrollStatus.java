package com.fwai.turtle.types;

public enum PayrollStatus {
    PENDING("待发放"),
    PAID("已发放"),
    CANCELLED("已取消");
    
    private final String description;

    PayrollStatus(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public static PayrollStatus get(String name) {
        for (PayrollStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 