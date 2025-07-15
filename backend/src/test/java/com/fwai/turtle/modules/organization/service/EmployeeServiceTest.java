package com.fwai.turtle.modules.organization.service;

import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.organization.dto.EmployeeDTO;
import com.fwai.turtle.modules.organization.entity.Employee;
import com.fwai.turtle.modules.organization.mapper.EmployeeEducationMapper;
import com.fwai.turtle.modules.organization.mapper.EmployeeJobHistoryMapper;
import com.fwai.turtle.modules.organization.mapper.EmployeeMapper;
import com.fwai.turtle.modules.organization.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @Mock
    private EmployeeEducationMapper employeeEducationMapper;
    @Mock
    private EmployeeJobHistoryMapper employeeJobHistoryMapper;
    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetById_found() {
        Employee employee = new Employee();
        employee.setId(1L);
        EmployeeDTO dto = new EmployeeDTO();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(dto);
        EmployeeDTO result = employeeService.getById(1L);
        assertNotNull(result);
        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    public void testGetById_notFound() {
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> employeeService.getById(2L));
    }

    @Test
    public void testCreate() {
        EmployeeDTO dto = new EmployeeDTO();
        Employee employee = new Employee();
        when(employeeMapper.toEntity(dto)).thenReturn(employee);
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toDTO(employee)).thenReturn(dto);
        EmployeeDTO result = employeeService.create(dto);
        assertNotNull(result);
        verify(employeeMapper).toEntity(dto);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toDTO(employee);
    }
} 