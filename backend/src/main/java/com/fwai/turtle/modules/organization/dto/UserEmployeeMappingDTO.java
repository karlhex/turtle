package com.fwai.turtle.modules.organization.dto;

import lombok.Data;

@Data
public class UserEmployeeMappingDTO {
    private Long userId;
    private Long employeeId;
    private String username;    // 用户名，用于显示
    private String employeeName; // 员工姓名，用于显示
}
