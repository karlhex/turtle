package com.fwai.turtle.modules.project.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fwai.turtle.modules.customer.dto.ProductDTO;

@Data
public class ContractItemDTO {
    private Long id;
    private Long contractId;
    private ProductDTO product;
    private String productName;  // 产品名称，用于显示
    private String modelNumber;  // 型号
    private Integer quantity;    // 数量
    private BigDecimal unitPrice; // 单价
    private BigDecimal totalAmount; // 总金额
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
