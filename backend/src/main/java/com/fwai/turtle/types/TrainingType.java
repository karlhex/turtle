package com.fwai.turtle.types;

public enum TrainingType {
    ONBOARDING("入职培训"),
    PROFESSIONAL("专业技能"),
    SAFETY("安全培训"),
    MANAGEMENT("管理培训"),
    CERTIFICATION("认证培训"),
    OTHER("其他");
    
    private final String description;

    TrainingType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static TrainingType get(String name) {
        for (TrainingType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 