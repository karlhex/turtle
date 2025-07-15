package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.modules.organization.dto.EmployeeJobHistoryDTO;
import java.util.List;

public interface EmployeeJobHistoryService {

    EmployeeJobHistoryDTO add(Long employeeId, EmployeeJobHistoryDTO jobHistoryDTO);

    EmployeeJobHistoryDTO update(Long employeeId, Long jobHistoryId, 
            EmployeeJobHistoryDTO jobHistoryDTO);

    List<EmployeeJobHistoryDTO> getByEmployeeId(Long employeeId);

    void delete(Long employeeId, Long jobHistoryId);

}
