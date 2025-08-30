package com.fwai.turtle.base.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.util.List;
import java.util.stream.Collectors;

import com.fwai.turtle.base.dto.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        
        return ApiResponse.error(400, String.join(", ", errors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ApiResponse.error(400, ex.getMessage());
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ApiResponse<Void> handleDuplicateRecordException(DuplicateRecordException ex) {
        return ApiResponse.error(30001, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<Void> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error(404, ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ApiResponse<Void> handleAuthenticationException(AuthenticationException ex) {
        return ApiResponse.error(401, ex.getMessage());
    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ApiResponse<Void> handleExpiredJwtException(ExpiredJwtException ex) {
        return ApiResponse.error(401, "Token expired");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<Void> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数类型错误: %s 应为 %s 类型", 
            ex.getName(), ex.getRequiredType().getSimpleName());
        return ApiResponse.error(40001, message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "请求数据格式错误";
        
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException ife = (InvalidFormatException) ex.getCause();
            message = String.format("参数格式错误: %s 应为 %s 类型", 
                ife.getPath().stream().map(ref -> ref.getFieldName()).findFirst().orElse("字段"),
                ife.getTargetType().getSimpleName());
        } else if (ex.getCause() instanceof MismatchedInputException) {
            MismatchedInputException mie = (MismatchedInputException) ex.getCause();
            message = String.format("参数输入不匹配: %s", 
                mie.getPath().stream().map(ref -> ref.getFieldName()).findFirst().orElse("字段"));
        }
        
        return ApiResponse.error(40001, message);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ApiResponse<Void> handleIllegalStateException(IllegalStateException ex) {
        return ApiResponse.error(40001, ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<Void> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = "数据完整性约束违反";
        
        // Extract the root cause message for more specific error handling
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null && rootCause.getMessage() != null) {
            String rootMessage = rootCause.getMessage().toLowerCase();
            
            if (rootMessage.contains("duplicate key") || rootMessage.contains("unique constraint")) {
                message = "数据已存在，请检查唯一性约束";
            } else if (rootMessage.contains("foreign key constraint")) {
                message = "关联数据不存在，请检查关联关系";
            } else if (rootMessage.contains("not null")) {
                message = "必填字段不能为空";
            } else if (rootMessage.contains("check constraint")) {
                message = "数据不符合约束条件";
            } else {
                message = "数据完整性错误: " + rootCause.getMessage();
            }
        } else {
            message = "数据操作违反约束: " + ex.getMessage();
        }
        
        return ApiResponse.error(40001, message);
    }
}