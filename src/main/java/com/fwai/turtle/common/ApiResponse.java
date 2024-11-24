package com.fwai.turtle.common;

import lombok.Data;

@Data
public class ApiResponse<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(T data) {
        ApiResponse<T> result = new ApiResponse<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> ApiResponse<T> error(Integer code, String message) {
        ApiResponse<T> result = new ApiResponse<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
