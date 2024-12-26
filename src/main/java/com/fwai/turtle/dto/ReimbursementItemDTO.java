package com.fwai.turtle.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.fwai.turtle.common.BaseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReimbursementItemDTO extends BaseDTO {
    private Long reimbursementId;  // 所属报销单ID
    private BigDecimal amount;  // 金额
    private String reason;  // 事由
    private LocalDate occurrenceDate;  // 发生时间
    private String remarks;  // 备注
}
