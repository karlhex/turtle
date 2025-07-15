package com.fwai.turtle.base.types;

public enum InventoryStatus {
    IN_STOCK("在库"),
    OUT_OF_STOCK("缺货"),
    DAMAGED("损坏"),
    LOST("丢失");
    
    private final String description;

    InventoryStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static InventoryStatus get(String name) {
        for (InventoryStatus e : values()) {
            if (e.name().equalsIgnoreCase(name)) {
                return e;
            }
        }
        return null;
    }
}
