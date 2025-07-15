package com.fwai.turtle.modules.finance.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CurrencyDTO {
    private Long id;
    private String code;
    private String name;
    private String symbol;
    private Integer decimalPlaces;
    private String country;
    private Boolean active;
    private Double exchangeRate;
    private Boolean isBaseCurrency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
