package com.fwai.turtle.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Person entity
 * 外部人员信息登记表
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @Column(name = "company_name")
    private String companyName;

    @Column
    private String department;

    @Column
    private String position;
} 