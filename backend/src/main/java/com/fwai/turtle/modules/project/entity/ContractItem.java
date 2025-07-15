package com.fwai.turtle.modules.project.entity;

import com.fwai.turtle.modules.project.entity.Contract;
import com.fwai.turtle.modules.customer.entity.Product;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

import com.fwai.turtle.base.entity.BaseEntity;


@Data
@Entity
@Table(name = "contract_items")
@EqualsAndHashCode(callSuper = true)
public class ContractItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "model_number", nullable = false)
    private String modelNumber;  // 型号

    @Column(nullable = false)
    private Integer quantity;  // 数量

    @Column(name = "unit_price", nullable = false, precision = 19, scale = 2)
    private BigDecimal unitPrice;  // 单价

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;  // 总金额

    @Column(length = 500)
    private String remarks;  // 备注

    @PrePersist
    @PreUpdate
    private void calculateTotalAmount() {
        if (quantity != null && unitPrice != null) {
            this.totalAmount = unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
