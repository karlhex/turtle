package com.fwai.turtle.dto;

import com.fwai.turtle.types.BankAccountType;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Bank Account DTO
 * 银行账户信息传输对象
 */
@Data
public class BankAccountDTO {
    private Long id;
    private String name;            // 账户名称
    private String accountNo;       // 账号
    private String bankName;        // 银行名称
    private BigDecimal balance;     // 余额
    private CurrencyDTO currency;   // 币种
    private BankAccountType type;   // 账户类型
    private String bankBranch;      // 开户行支行
    private String description;     // 描述
    private Boolean active;         // 是否启用
    private String swiftCode;       // SWIFT代码
    private String contactPerson;   // 联系人
    private String contactPhone;    // 联系电话
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
