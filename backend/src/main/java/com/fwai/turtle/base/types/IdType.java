package com.fwai.turtle.base.types;

public enum IdType {
    ID_CARD("身份证"),
    PASSPORT("护照"),
    WORK_PERMIT("工作许可证"),
    OTHER("其他");
    
    private final String description;

    IdType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static IdType get(String name) {
        for (IdType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 