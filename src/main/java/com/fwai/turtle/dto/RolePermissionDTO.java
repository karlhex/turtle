package com.fwai.turtle.dto;

import com.fwai.turtle.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RolePermissionDTO extends BaseDTO {
    private String roleName;
    private String permission;
}
