package com.fwai.turtle.base.types;

public enum TrainingStatus {
    PLANNED("计划中"),
    ONGOING("进行中"),
    COMPLETED("已完成"),
    CANCELLED("已取消");
    
    private final String description;

    TrainingStatus(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public static TrainingStatus get(String name) {
        for (TrainingStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 