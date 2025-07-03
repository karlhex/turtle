package com.fwai.turtle.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReimbursementItemDTO(
    Long id,
    Long reimbursementId,  // 所属报销单ID
    BigDecimal amount,  // 金额
    String reason,  // 事由
    LocalDate occurrenceDate,  // 发生时间
    String remarks,  // 备注
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public ReimbursementItemDTO {
        if (amount == null) {
            throw new IllegalArgumentException("金额不能为空");
        }
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("事由不能为空");
        }
        if (occurrenceDate == null) {
            throw new IllegalArgumentException("发生时间不能为空");
        }
    }
}
