package com.fwai.turtle.persistence.entity;

public enum DebitCreditType {
    DEBIT("借"), 
    CREDIT("贷");

    private final String description;

    DebitCreditType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
