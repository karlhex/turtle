package com.fwai.turtle.modules.project.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fwai.turtle.base.types.ProjectStatus;
import com.fwai.turtle.modules.organization.dto.EmployeeDTO;

@Data
public class ProjectDTO {
    private Long id;
    private String projectName;
    private String projectNo;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private EmployeeDTO manager;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private List<ContractDTO> contracts = new ArrayList<>();  // 关联的合同列表
}
