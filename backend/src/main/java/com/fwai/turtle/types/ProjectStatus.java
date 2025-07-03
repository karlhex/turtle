package com.fwai.turtle.types;

public enum ProjectStatus {
    PLANNING("规划中"),
    IN_PROGRESS("进行中"),
    COMPLETED("已完成"),
    SUSPENDED("已暂停"),
    CANCELLED("已取消");
    
    private final String description;

    ProjectStatus(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public static ProjectStatus get(String name) {
        for (ProjectStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
