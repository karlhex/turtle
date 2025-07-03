package com.fwai.turtle.dto;

import com.fwai.turtle.common.BaseDTO;
import com.fwai.turtle.types.InvoiceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class InvoiceDTO extends BaseDTO {
    private String invoiceNo;
    private TaxInfoDTO buyerTaxInfo;
    private TaxInfoDTO sellerTaxInfo;
    private String batchNo;
    private InvoiceType type;
    private BigDecimal amount;
    private BigDecimal taxRate;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private String remarks;
    private LocalDate invoiceDate;
    private String verificationCode;
    private String machineCode;
    private Boolean cancelled;
    private LocalDate cancelDate;
    private String cancelReason;
}
