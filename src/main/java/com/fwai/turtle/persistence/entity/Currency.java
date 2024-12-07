package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "currencies")
@EqualsAndHashCode(callSuper = true)
public class Currency extends BaseEntity {

    @Column(nullable = false, unique = true, length = 3)
    private String code;  // ISO 4217 currency code (e.g., USD, EUR, CNY)

    @Column(nullable = false)
    private String name;  // Full name of the currency

    @Column(nullable = false)
    private String symbol;  // Currency symbol (e.g., $, €, ¥)

    @Column(name = "decimal_places", nullable = false)
    private Integer decimalPlaces;  // Number of decimal places for the currency

    @Column
    private String country;  // Country where the currency is primarily used

    @Column
    private Boolean active = true;  // Whether the currency is active in the system

    @Column(name = "exchange_rate")
    private Double exchangeRate;  // Exchange rate relative to the base currency

    @Column(name = "is_base_currency")
    private Boolean isBaseCurrency = false;  // Whether this is the system's base currency
}
