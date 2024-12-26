package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.common.BaseEntity;
import com.fwai.turtle.types.ReimbursementStatus;

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

    @Column(name = "applicant_id", nullable = false)
    private Long applicantId;  // 申请员工ID

    @Column(name = "approval_date")
    private LocalDate approvalDate;  // 审批日期

    @Column(name = "approver_id")
    private Long approverId;  // 审批员工ID

    @Column(name = "approval_comments")
    private String approvalComments;  // 审批意见

    @Column(name = "project_id", nullable = false)
    private Long projectId;  // 关联项目ID

    @Column(name = "total_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;  // 总金额

    @OneToMany(mappedBy = "reimbursement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReimbursementItem> items = new ArrayList<>();  // 报销单项列表

    @Column
    private String remarks;  // 备注

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReimbursementStatus status;  // 状态：DRAFT-草稿, PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝

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
