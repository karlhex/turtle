package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.EmployeeJobHistoryDTO;
import com.fwai.turtle.common.Result;

import java.util.List;

public interface EmployeeJobHistoryService {

    Result<EmployeeJobHistoryDTO> add(Long employeeId, EmployeeJobHistoryDTO jobHistoryDTO);

    Result<EmployeeJobHistoryDTO> update(Long employeeId, Long jobHistoryId, 
            EmployeeJobHistoryDTO jobHistoryDTO);

    Result<List<EmployeeJobHistoryDTO>> getByEmployeeId(Long employeeId);

    Result<Void> delete(Long employeeId, Long jobHistoryId);

}
