package com.fwai.turtle.modules.finance.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "reimbursement_items")
@EqualsAndHashCode(callSuper = true)
public class ReimbursementItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reimbursement_id", nullable = false)
    private Reimbursement reimbursement;  // 所属报销单

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;  // 金额

    @Column(nullable = false)
    private String reason;  // 事由

    @Column(name = "occurrence_date", nullable = false)
    private LocalDate occurrenceDate;  // 发生时间

    @Column
    private String remarks;  // 备注
}
