package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "reimbursements")
@EqualsAndHashCode(callSuper = true)
public class Reimbursement extends BaseEntity {

    @Column(name = "reimbursement_no", nullable = false, unique = true)
    private String reimbursementNo;  // 报销单号

    @Column(name = "application_date", nullable = false)
    private LocalDate applicationDate;  // 申请日期

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    private Employee applicant;  // 申请员工

    @Column(name = "approval_date")
    private LocalDate approvalDate;  // 审批日期

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private Employee approver;  // 审批员工

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;  // 关联项目

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;  // 总金额

    @OneToMany(mappedBy = "reimbursement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReimbursementItem> items = new ArrayList<>();  // 报销单项列表

    @Column
    private String remarks;  // 备注

    @PrePersist
    @PreUpdate
    private void calculateTotalAmount() {
        if (items != null && !items.isEmpty()) {
            this.totalAmount = items.stream()
                .map(ReimbursementItem::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
