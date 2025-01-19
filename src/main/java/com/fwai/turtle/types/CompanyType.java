package com.fwai.turtle.types;

public enum CompanyType {
    PRIMARY("主公司"),  // 主公司
    CUSTOMER("客户"), // 客户
    SUPPLIER("供应商"),  // 供应商
    PROSPECT("潜在客户");  // 潜在客户

    final String description;

    CompanyType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
