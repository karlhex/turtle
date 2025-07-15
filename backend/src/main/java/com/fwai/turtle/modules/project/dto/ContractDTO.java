package com.fwai.turtle.modules.project.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fwai.turtle.base.dto.BaseDTO;
import com.fwai.turtle.base.types.ContractStatus;
import com.fwai.turtle.base.types.ContractType;
import com.fwai.turtle.modules.customer.dto.CompanyDTO;
import com.fwai.turtle.modules.finance.dto.CurrencyDTO;
import com.fwai.turtle.modules.project.dto.ContractItemDTO;
import com.fwai.turtle.modules.project.dto.ContractDownPaymentDTO;
import com.fwai.turtle.modules.finance.dto.InvoiceDTO;
import com.fwai.turtle.modules.customer.dto.ContactDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContractDTO extends BaseDTO {
    private String contractNo;
    private String title;
    private CompanyDTO buyerCompany;
    private CompanyDTO sellerCompany;
    private ContractType type;
    private LocalDate signingDate;
    private LocalDate startDate;
    private LocalDate endDate;
    private ContactDTO contactPerson;
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

    private List<ContractItemDTO> items = new ArrayList<>();  // 合同产品列表
    private List<ContractDownPaymentDTO> downPayments = new ArrayList<>();  // 合同首付款列表
    private List<InvoiceDTO> invoices = new ArrayList<>();  // 关联发票列表
}
