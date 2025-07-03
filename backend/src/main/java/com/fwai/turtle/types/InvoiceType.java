package com.fwai.turtle.types;

public enum InvoiceType {
    VAT_NORMAL("增值税普通发票"),
    VAT_SPECIAL("增值税专用发票");

    private final String description;

    InvoiceType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
