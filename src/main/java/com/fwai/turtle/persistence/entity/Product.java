package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;
import com.fwai.turtle.types.ProductType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "products")
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;  // 产品名称

    @Column(name = "model_number", nullable = false)
    private String modelNumber;  // 产品型号

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id", nullable = false)
    private Company manufacturer;  // 生产公司

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;  // 产品类型

    @Column(nullable = false)
    private String unit;  // 单位

    @Column(length = 1000)
    private String description;  // 产品描述

    @Column
    private String specifications;  // 规格参数

    @Column
    private String remarks;  // 备注

    @Column
    private Boolean active = true;  // 是否启用

    @Column(name = "warranty_period")
    private Integer warrantyPeriod;  // 质保期（天）
}
