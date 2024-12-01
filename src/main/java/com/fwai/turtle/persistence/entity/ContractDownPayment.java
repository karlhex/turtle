package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "contract_down_payments")
@EqualsAndHashCode(callSuper = true)
public class ContractDownPayment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "payment_instruction", nullable = false, length = 500)
    private String paymentInstruction;  // 首付款指令

    @Enumerated(EnumType.STRING)
    @Column(name = "debit_credit_type", nullable = false)
    private DebitCreditType debitCreditType;  // 借贷方向

    @Column(name = "planned_date", nullable = false)
    private LocalDate plannedDate;  // 计划首付款日期

    @Column(name = "actual_date")
    private LocalDate actualDate;  // 实际发生日期

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;  // 金额

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;  // 币种

    @Column(name = "bank_account_no")
    private String bankAccountNo;  // 银行账号

    @Column(name = "bank_name")
    private String bankName;  // 开户行

    @Column(name = "account_name")
    private String accountName;  // 账户名

    @Column(length = 1000)
    private String remarks;  // 备注

    @Column(name = "payment_status")
    private Boolean paymentStatus = false;  // 支付状态，false: 未支付，true: 已支付

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (actualDate != null) {
            this.paymentStatus = true;
        }
    }
}
