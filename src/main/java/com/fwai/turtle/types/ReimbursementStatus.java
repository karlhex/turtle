package com.fwai.turtle.types;

public enum ReimbursementStatus {
    DRAFT("草稿"),
    PENDING("待审批"),
    APPROVED("已通过"),
    REJECTED("已拒绝");
    
    private final String description;

    ReimbursementStatus(String description) {
        this.description = description;
    }



    public String getDescription() {
        return description;
    }

    public static ReimbursementStatus get(String name) {
        for (ReimbursementStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
