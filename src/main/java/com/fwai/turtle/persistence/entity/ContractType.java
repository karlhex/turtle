package com.fwai.turtle.persistence.entity;

public enum ContractType {
    PURCHASE("采购合同"),
    SALES("销售合同");

    private final String description;

    ContractType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
