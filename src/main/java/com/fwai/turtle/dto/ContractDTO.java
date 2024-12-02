package com.fwai.turtle.dto;

import com.fwai.turtle.persistence.entity.ContractStatus;
import com.fwai.turtle.persistence.entity.ContractType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ContractDTO {
    private Long id;
    private String contractNo;
    private String title;
    private String buyerCompany;
    private String sellerCompany;
    private ContractType type;
    private LocalDate signingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private BigDecimal totalAmount;
    private Long currencyId;
    private String currencyCode;  // 币种代码，用于显示
    private ContractStatus status;
    private String projectNo;
    private Long projectId;
    private String description;
    private String remarks;
    private String paymentTerms;
    private String deliveryTerms;
    private String warrantyTerms;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ContractItemDTO> items = new ArrayList<>();  // 合同产品列表
    private List<ContractDownPaymentDTO> downPayments = new ArrayList<>();  // 合同首付款列表
}
