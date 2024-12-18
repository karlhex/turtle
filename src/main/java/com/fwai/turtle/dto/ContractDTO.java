package com.fwai.turtle.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fwai.turtle.types.ContractStatus;
import com.fwai.turtle.types.ContractType;

@Data
public class ContractDTO {
    private Long id;
    private String contractNo;
    private String title;
    private CompanyDTO buyerCompany;
    private CompanyDTO sellerCompany;
    private ContractType type;
    private LocalDate signingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private PersonDTO contactPerson;
    private BigDecimal totalAmount;
    private CurrencyDTO currency;
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
    private List<InvoiceDTO> invoices = new ArrayList<>();  // 关联发票列表
}
