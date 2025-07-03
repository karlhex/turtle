package com.fwai.turtle.types;

public enum ProductType {
    SOFTWARE("软件产品"),
    HARDWARE("硬件产品"),
    SERVICE("服务产品");

    private final String description;

    ProductType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
