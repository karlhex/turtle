package com.fwai.turtle.types;

public enum ClientType {
    CONTRACT("合同"),
    PROJECT("项目"),
    PRODUCT("产品"),
    USER("用户");
    
    private final String description;

    ClientType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ClientType get(String name) {
        for (ClientType e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
