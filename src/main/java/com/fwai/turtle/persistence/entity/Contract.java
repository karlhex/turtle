package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;
import com.fwai.turtle.types.ContractStatus;
import com.fwai.turtle.types.ContractType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "contracts")
@EqualsAndHashCode(callSuper = true)
public class Contract extends BaseEntity {

    @Column(name = "contract_no", nullable = false, unique = true)
    private String contractNo;  // 合同编号

    @Column(nullable = false)
    private String title;  // 标题

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_company_id", nullable = false)
    private Company buyerCompany;  // 买方公司

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_company_id", nullable = false)
    private Company sellerCompany;  // 卖方公司

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType type;  // 合同类型

    @Column(name = "signing_date", nullable = false)
    private LocalDate signingDate;  // 签署日期

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;  // 开始日期

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;  // 结束日期

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;  // 联系人

    @Column(name = "contact_phone")
    private String contactPhone;  // 联系电话

    @Column(name = "contact_email")
    private String contactEmail;  // 联系邮箱

    @Column(name = "project_id")
    private Long projectId;  // 关联的项目

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;  // 合同总金额

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private Currency currency;  // 币种

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;  // 合同状态

    @Column(name = "project_no")
    private String projectNo;  // 项目编号

    @Column(length = 1000)
    private String description;  // 合同描述

    @Column
    private String remarks;  // 备注

    @Column(name = "payment_terms", length = 1000)
    private String paymentTerms;  // 付款条款

    @Column(name = "delivery_terms", length = 1000)
    private String deliveryTerms;  // 交付条款

    @Column(name = "warranty_terms", length = 1000)
    private String warrantyTerms;  // 质保条款

    @Column(name = "file_path")
    private String filePath;  // 合同文件路径

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractItem> items = new ArrayList<>();  // 合同产品列表

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContractDownPayment> downPayments = new ArrayList<>();  // 合同首付款列表

    @PrePersist
    @PreUpdate
    private void validateDates() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new IllegalStateException("合同开始日期不能晚于结束日期");
        }
        if (signingDate != null && startDate != null && signingDate.isAfter(startDate)) {
            throw new IllegalStateException("签署日期不能晚于开始日期");
        }
    }
}
