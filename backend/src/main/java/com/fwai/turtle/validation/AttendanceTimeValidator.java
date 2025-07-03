package com.fwai.turtle.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalTime;

import com.fwai.turtle.dto.EmployeeAttendanceDTO;

public class AttendanceTimeValidator implements ConstraintValidator<ValidAttendanceTime, EmployeeAttendanceDTO> {
    @Override
    public boolean isValid(EmployeeAttendanceDTO dto, ConstraintValidatorContext context) {
        if (dto.checkInTime() == null) {
            return true;
        }

        // 检查签到时间是否在合理范围内（例如：6:00-11:00）
        LocalTime minCheckInTime = LocalTime.of(6, 0);
        LocalTime maxCheckInTime = LocalTime.of(11, 0);
        if (dto.checkInTime().isBefore(minCheckInTime) || dto.checkInTime().isAfter(maxCheckInTime)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("签到时间必须在6:00-11:00之间")
                  .addPropertyNode("checkInTime")
                  .addConstraintViolation();
            return false;
        }

        // 如果有签退时间，检查是否在签到时间之后
        if (dto.checkOutTime() != null && dto.checkOutTime().isBefore(dto.checkInTime())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("签退时间必须在签到时间之后")
                  .addPropertyNode("checkOutTime")
                  .addConstraintViolation();
            return false;
        }

        return true;
    }
} 