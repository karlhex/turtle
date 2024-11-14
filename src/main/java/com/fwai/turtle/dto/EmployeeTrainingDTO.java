package com.fwai.turtle.dto;

import com.fwai.turtle.types.TrainingStatus;
import com.fwai.turtle.types.TrainingType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * EmployeeTraining DTO
 * 员工培训记录DTO
 */
@Data
public class EmployeeTrainingDTO {
    private Long id;

    private Long employeeId;              // 关联员工ID

    private String trainingName;          // 培训名称

    private LocalDateTime startTime;      // 开始时间

    private LocalDateTime endTime;        // 结束时间

    private TrainingType type;            // 培训类型

    private String trainer;               // 培训讲师

    private String location;              // 培训地点

    private String description;           // 培训描述

    private String attachments;           // 培训材料附件

    private Integer score;                // 培训成绩

    private TrainingStatus status;        // 培训状态

    private String remarks;               // 备注
}

