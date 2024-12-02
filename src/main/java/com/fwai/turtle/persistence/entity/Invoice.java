package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "invoices")
@EqualsAndHashCode(callSuper = true)
public class Invoice extends BaseEntity {

    @Column(name = "invoice_no", nullable = false, unique = true)
    private String invoiceNo;  // 发票号码

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_tax_info_id", nullable = false)
    private TaxInfo buyerTaxInfo;  // 买方税务信息

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_tax_info_id", nullable = false)
    private TaxInfo sellerTaxInfo;  // 卖方税务信息

    @Column(name = "batch_no")
    private String batchNo;  // 批次号

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceType type;  // 发票类型

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;  // 金额

    @Column(name = "tax_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxRate;  // 税率

    @Column(name = "tax_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal taxAmount;  // 税额

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;  // 价税合计

    @Column
    private String remarks;  // 备注

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;  // 开票日期

    @Column(name = "verification_code")
    private String verificationCode;  // 校验码

    @Column(name = "machine_code")
    private String machineCode;  // 机器编号

    @Column
    private Boolean cancelled = false;  // 是否作废

    @Column(name = "cancel_date")
    private LocalDate cancelDate;  // 作废日期

    @Column(name = "cancel_reason")
    private String cancelReason;  // 作废原因

    @PrePersist
    @PreUpdate
    private void calculateAmounts() {
        if (amount != null && taxRate != null) {
            // 计算税额
            this.taxAmount = amount.multiply(taxRate.divide(new BigDecimal("100")))
                .setScale(2, java.math.RoundingMode.HALF_UP);
            // 计算价税合计
            this.totalAmount = amount.add(taxAmount);
        }
    }
}