package com.fwai.turtle.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AttendanceTimeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAttendanceTime {
    String message() default "考勤时间无效";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 