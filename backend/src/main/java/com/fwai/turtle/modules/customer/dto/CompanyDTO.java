package com.fwai.turtle.modules.customer.dto;

import lombok.Data;
import java.time.LocalDateTime;
import com.fwai.turtle.base.types.CompanyType;
import java.util.List;
import com.fwai.turtle.modules.finance.dto.BankAccountDTO;
import com.fwai.turtle.modules.finance.dto.TaxInfoDTO;
import com.fwai.turtle.modules.customer.dto.ContactDTO;

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
    private List<BankAccountDTO> bankAccounts;  // 公司银行账户列表
    private TaxInfoDTO taxInfo;     // 税务信息
    private Boolean active;         // 是否启用
    private String remarks;         // 备注
    private CompanyType type;       // 公司类型
    private List<ContactDTO> contacts;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
