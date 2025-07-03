package com.fwai.turtle.persistence.entity;

import com.fwai.turtle.types.CompanyType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fwai.turtle.common.BaseEntity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Company entity
 * 公司信息表
 */
@Data
@Entity
@Table(name = "companies")
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseEntity {

    @Column(name = "full_name", nullable = false)
    private String fullName;  // 公司全称

    @Column(name = "short_name")
    private String shortName;  // 公司简称

    @Column(nullable = false)
    private String address;  // 公司地址

    @Column(nullable = false)
    private String phone;  // 公司电话

    @Column
    private String email;  // 公司邮件

    @Column
    private String website;  // 公司网站

    @Column
    private CompanyType type;  // 公司类型

    @OneToMany(mappedBy = "companyId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BankAccount> bankAccounts = new ArrayList<>();  // 公司银行账户

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tax_info_id")
    private TaxInfo taxInfo;  // 税务信息

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"company"})
    private List<Contact> contacts = new ArrayList<>();  // 公司联系人

    @Column
    private Boolean active = true;  // 是否启用

    @Column
    private String remarks;  // 备注
}
