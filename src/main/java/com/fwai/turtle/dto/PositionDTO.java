package com.fwai.turtle.dto;

import lombok.Data;

import com.fwai.turtle.common.BaseDTO;

import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class PositionDTO extends BaseDTO {
    private String name;
    private String description;
    private String code;
    private Boolean isActive;
    private Long departmentId;
    private String departmentName;
    private Integer level;
    private String responsibilities;
}
