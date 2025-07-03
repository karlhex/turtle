package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;
import com.fwai.turtle.types.BankAccountType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Person entity
 * 外部人员信息登记表
 */
@Data
@Entity
@Table(name = "bank_accounts")
@EqualsAndHashCode(callSuper = true)
public class BankAccount extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(name = "account_no", nullable = false, unique = true)
    private String accountNo;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;

    @Column(name = "company_id")
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankAccountType type;

    @Column(name = "bank_branch")
    private String bankBranch;

    @Column
    private String description;

    @Column
    private Boolean active = true;

    @Column(name = "swift_code")
    private String swiftCode;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "contact_phone")
    private String contactPhone;
}