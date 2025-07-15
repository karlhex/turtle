package com.fwai.turtle.modules.organization.dto;

import lombok.Data;

import com.fwai.turtle.base.dto.BaseDTO;

import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class PositionDTO extends BaseDTO {
    private String name;
    private String description;
    private String code;
    private Boolean isActive;
    private Integer level;
    private String responsibilities;
}
