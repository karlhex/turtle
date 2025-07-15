package com.fwai.turtle.modules.project.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fwai.turtle.base.types.DebitCreditType;

@Data
public class ContractDownPaymentDTO {
    private Long id;
    private Long contractId;
    private String contractNo;  // 合同编号，用于显示
    private String paymentInstruction;
    private DebitCreditType debitCreditType;
    private LocalDate plannedDate;
    private LocalDate actualDate;
    private BigDecimal amount;
    private Long currencyId;
    private String currencyCode;  // 币种代码，用于显示
    private String bankAccountNo;
    private String bankName;
    private String accountName;
    private String remarks;
    private Boolean paymentStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
