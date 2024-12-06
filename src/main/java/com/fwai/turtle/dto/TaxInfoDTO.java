package com.fwai.turtle.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * Tax Info DTO
 * 税务信息传输对象
 */
@Data
public class TaxInfoDTO {
    private Long id;
    private String fullName;        // 公司全称
    private String address;         // 地址
    private String phone;           // 电话
    private String bankName;        // 开户行
    private String bankCode;        // 联行号
    private String bankAccount;     // 银行账号
    private String taxNo;          // 税号
    private Boolean active;        // 是否启用
    private String remarks;        // 备注
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
