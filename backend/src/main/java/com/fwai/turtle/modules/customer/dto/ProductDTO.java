package com.fwai.turtle.modules.customer.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.fwai.turtle.base.types.ProductType;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String modelNumber;
    private CompanyDTO manufacturer;
    private ProductType type;
    private String unit;
    private String description;
    private String specifications;
    private String remarks;
    private Boolean active;
    private Integer warrantyPeriod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
