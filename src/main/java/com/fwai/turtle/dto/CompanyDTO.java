package com.fwai.turtle.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Company DTO
 * 公司信息传输对象
 */
@Data
public class CompanyDTO {
    private Long id;
    private String fullName;        // 公司全称
    private String shortName;       // 公司简称
    private String address;         // 公司地址
    private String phone;           // 公司电话
    private String email;           // 公司邮件
    private String website;         // 公司网站
    private Boolean isPrimary;      // 主公司标志
    private List<BankAccountDTO> bankAccounts;  // 公司银行账户列表
    private TaxInfoDTO taxInfo;     // 税务信息
    private PersonDTO businessContact;   // 商务联系人
    private PersonDTO technicalContact;  // 技术联系人
    private Boolean active;         // 是否启用
    private String remarks;         // 备注
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
