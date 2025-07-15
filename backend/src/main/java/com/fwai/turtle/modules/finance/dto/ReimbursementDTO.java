package com.fwai.turtle.modules.finance.dto;

import com.fwai.turtle.base.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReimbursementDTO extends BaseDTO {
    // 基本信息
    private String reimbursementNo;  // 报销单号
    private LocalDate applicationDate;  // 申请日期
    private BigDecimal totalAmount;  // 总金额
    private String remarks;  // 备注
    private String status;  // 状态：DRAFT-草稿, PENDING-待审批, APPROVED-已通过, REJECTED-已拒绝

    // 申请人信息
    private Long applicantId;  // 申请员工ID

    // 审批人信息
    private Long approverId;  // 审批员工ID
    private LocalDate approvalDate;  // 审批日期

    // 项目信息
    private Long projectId;  // 关联项目ID

    // 报销明细
    private List<ReimbursementItemDTO> items = new ArrayList<>();  // 报销单项列表
}
