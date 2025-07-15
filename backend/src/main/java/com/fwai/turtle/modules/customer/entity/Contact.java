package com.fwai.turtle.modules.customer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.fwai.turtle.base.entity.BaseEntity;
import lombok.EqualsAndHashCode;

/**
 * Contact entity
 * 联系人信息登记表
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@Table(name = "contacts")
public class Contact extends BaseEntity {
    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String address;

    @Column
    private String email;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "work_phone")
    private String workPhone;

    @Column(name = "home_phone")
    private String homePhone;

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Company company;

    @Column
    private String department; // 部门

    @Column
    private String title; // 职位

    @Column
    private String gender;   // 性别

    @Column
    private String nativePlace;   // 籍贯

    @Column
    private String ethnicity;   // 民族

    @Column
    private String maritalStatus;   // 婚姻状况

    @Column
    private String nationality;   // 国籍

    @Column
    private String birthDate;   // 出生日期

    @Column
    private String religion;    // 宗教

    @Column
    private String university;   // 毕业院校

    @Column
    private String major;   // 专业

    @Column
    private String graduationYear;   // 毕业年份

    @Column
    private String degree;   // 学位

    @Column
    private String hobbies;   // 爱好

    @Column(length = 1000)
    private String remarks;   // 备注

    @Column
    @Builder.Default
    private Boolean active = true;
}