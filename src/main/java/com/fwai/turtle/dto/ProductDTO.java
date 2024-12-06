package com.fwai.turtle.dto;

import lombok.Data;
import java.time.LocalDateTime;

import com.fwai.turtle.types.ProductType;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String modelNumber;
    private String manufacturer;
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
