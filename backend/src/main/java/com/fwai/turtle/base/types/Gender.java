package com.fwai.turtle.base.types;

public enum Gender {
    MALE("男"),
    FEMALE("女"),
    OTHER("其他");
    
    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Gender get(String name) {
        for (Gender e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 