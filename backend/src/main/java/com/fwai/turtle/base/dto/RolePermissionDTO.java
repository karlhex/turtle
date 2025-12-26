package com.fwai.turtle.base.dto;

import com.fwai.turtle.base.dto.BaseDTO;
import com.fwai.turtle.base.enums.PermissionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermissionDTO extends BaseDTO {
    private String roleName;
    private String transactionPattern;
    private String description;
    private PermissionType permissionType = PermissionType.SPECIFIC;
    private Boolean isActive = true;
}
