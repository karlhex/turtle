package com.fwai.turtle.modules.finance.entity;

import com.fwai.turtle.base.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "tax_infos")
@EqualsAndHashCode(callSuper = true)
public class TaxInfo extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;  // 公司全称

    @Column(nullable = false)
    private String address;  // 地址

    @Column(nullable = false)
    private String phone;  // 电话

    @Column(name = "bank_name", nullable = false)
    private String bankName;  // 开户行

    @Column(name = "bank_code", nullable = false)
    private String bankCode;  // 联行号

    @Column(name = "bank_account", nullable = false, unique = true)
    private String bankAccount;  // 银行账号

    @Column(name = "tax_no", nullable = false, unique = true)
    private String taxNo;  // 税号

    @Column
    private Boolean active = true;  // 是否启用

    @Column
    private String remarks;  // 备注
}
