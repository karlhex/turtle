package com.fwai.turtle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotBlank(message = "当前密码不能为空")
    private String currentPassword;

    @NotBlank(message = "新密码不能为空")
    @Size(min = 8, max = 32, message = "新密码长度必须在8-32个字符之间")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$",
        message = "新密码必须包含至少一个大写字母、一个小写字母、一个数字和一个特殊字符"
    )
    private String newPassword;

    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}
