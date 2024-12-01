package com.fwai.turtle.persistence.entity;

public enum BankAccountType {
    COMPANY_BASIC("公司基本账户"),
    COMPANY_GENERAL("公司往来账户"),
    COMPANY_OTHER("其他公司账户"),
    PERSONAL("个人账户");

    private final String description;

    BankAccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
