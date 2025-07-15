package com.fwai.turtle.modules.organization.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDTO {
    private Long id;
    private String name;        // 部门名称
    private String description; // 部门描述
    private String code;        // 部门编码
    private Boolean isActive;   // 是否激活
    private Long parentId;      // 父部门ID
    private Long managerId;     // 部门管理人员
}
