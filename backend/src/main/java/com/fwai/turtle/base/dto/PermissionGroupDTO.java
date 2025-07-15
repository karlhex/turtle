package com.fwai.turtle.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionGroupDTO {
    private Long id;

    @NotBlank(message = "权限组名称不能为空")
    @Size(min = 2, max = 50, message = "权限组名称长度必须在2-50个字符之间")
    private String name;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    private Set<Long> roleIds;
    private Set<Long> permissionIds;
    private boolean active;
}
