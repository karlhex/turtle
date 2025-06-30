package com.fwai.turtle.types;

public enum EmployeeContractType {

    FIXED_TERM("固定期限"),
    NON_FIXED_TERM("无固定期限"),
    INTERNSHIP("实习"),
    PART_TIME("兼职"),
    PROBATION("试用期");
    
    
    private final String description;

    EmployeeContractType(String description) {
        this.description = description;
    }
    // 试用期

    public String getDescription() {
        return description;
    }

    public static EmployeeContractType get(String name) {
        for (EmployeeContractType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
} 