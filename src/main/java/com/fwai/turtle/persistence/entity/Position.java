package com.fwai.turtle.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;
import com.fwai.turtle.common.BaseEntity;
import lombok.EqualsAndHashCode;


/**
 * Position entity
 * 职位信息表
 */
@Data
@Entity
@Table(name = "positions")
@EqualsAndHashCode(callSuper = true)
public class Position extends BaseEntity {

    @Column(nullable = false)
    private String name;        // 职位名称

    @Column
    private String description; // 职位描述

    @Column
    private String code;        // 职位编码

    @Column
    private Boolean isActive = true;  // 是否激活

    @Column
    private Integer level;      // 职级

    @Column
    private String responsibilities; // 职责描述
}
