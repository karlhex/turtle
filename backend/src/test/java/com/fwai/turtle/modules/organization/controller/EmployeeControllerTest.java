package com.fwai.turtle.modules.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.organization.dto.*;
import com.fwai.turtle.modules.organization.service.*;
import com.fwai.turtle.base.types.EmployeeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fwai.turtle.modules.organization.service.impl.EmployeeAttendanceServiceImpl;
import com.fwai.turtle.security.service.JwtTokenService;
import com.fwai.turtle.security.service.TokenBlacklistService;

@WebMvcTest(
    controllers = EmployeeController.class,
    excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
    }
)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private EmployeeEducationService employeeEducationService;
    @MockBean
    private EmployeeJobHistoryService employeeJobHistoryService;
    @MockBean
    private EmployeeAttendanceService employeeAttendanceService;
    @MockBean
    private EmployeeLeaveService employeeLeaveService;
    @MockBean
    private EmployeePayrollService employeePayrollService;
    @MockBean
    private EmployeeAttendanceServiceImpl employeeAttendanceServiceImpl;
    @MockBean
    private JwtTokenService jwtTokenService;
    @MockBean
    private TokenBlacklistService tokenBlacklistService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/employees should return 200 and employee list")
    public void testGetAllEmployees_Success() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("张三");
        employee.setStatus(EmployeeStatus.ACTIVE);
        Page<EmployeeDTO> page = new PageImpl<>(Collections.singletonList(employee), PageRequest.of(0, 10), 1);
        when(employeeService.getAll(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);
        mockMvc.perform(get("/api/employees")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "id")
                .param("direction", "ASC"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /api/employees should create employee successfully")
    public void testCreateEmployee_Success() throws Exception {
        EmployeeDTO inputEmployee = new EmployeeDTO();
        inputEmployee.setName("李四");
        inputEmployee.setEmail("lisi@example.com");
        inputEmployee.setStatus(EmployeeStatus.ACTIVE);

        EmployeeDTO createdEmployee = new EmployeeDTO();
        createdEmployee.setId(1L);
        createdEmployee.setName("李四");
        createdEmployee.setEmail("lisi@example.com");
        createdEmployee.setStatus(EmployeeStatus.ACTIVE);

        when(employeeService.create(any(EmployeeDTO.class))).thenReturn(createdEmployee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("李四"))
                .andExpect(jsonPath("$.data.email").value("lisi@example.com"));
    }

    @Test
    @DisplayName("PUT /api/employees/{id} should update employee successfully")
    public void testUpdateEmployee_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeDTO inputEmployee = new EmployeeDTO();
        inputEmployee.setName("王五");
        inputEmployee.setEmail("wangwu@example.com");
        inputEmployee.setStatus(EmployeeStatus.ACTIVE);

        EmployeeDTO updatedEmployee = new EmployeeDTO();
        updatedEmployee.setId(employeeId);
        updatedEmployee.setName("王五");
        updatedEmployee.setEmail("wangwu@example.com");
        updatedEmployee.setStatus(EmployeeStatus.ACTIVE);

        when(employeeService.update(eq(employeeId), any(EmployeeDTO.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(employeeId))
                .andExpect(jsonPath("$.data.name").value("王五"))
                .andExpect(jsonPath("$.data.email").value("wangwu@example.com"));
    }

    @Test
    @DisplayName("GET /api/employees/{id} should return employee by id")
    public void testGetEmployeeById_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(employeeId);
        employee.setName("赵六");
        employee.setEmail("zhaoliu@example.com");
        employee.setStatus(EmployeeStatus.ACTIVE);

        when(employeeService.getById(employeeId)).thenReturn(employee);

        mockMvc.perform(get("/api/employees/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(employeeId))
                .andExpect(jsonPath("$.data.name").value("赵六"))
                .andExpect(jsonPath("$.data.email").value("zhaoliu@example.com"));
    }

    @Test
    @DisplayName("GET /api/employees/search should return search results")
    public void testSearchEmployees_Success() throws Exception {
        String query = "张三";
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("张三");
        employee.setStatus(EmployeeStatus.ACTIVE);
        Page<EmployeeDTO> page = new PageImpl<>(Collections.singletonList(employee), PageRequest.of(0, 10), 1);

        when(employeeService.search(eq(query), anyInt(), anyInt())).thenReturn(page);

        mockMvc.perform(get("/api/employees/search")
                .param("query", query)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].name").value("张三"));
    }

    @Test
    @DisplayName("DELETE /api/employees/{id} should delete employee successfully")
    public void testDeleteEmployee_Success() throws Exception {
        Long employeeId = 1L;

        mockMvc.perform(delete("/api/employees/{id}", employeeId))
                .andExpect(status().isOk());

        verify(employeeService).delete(employeeId);
    }

    @Test
    @DisplayName("GET /api/employees/unmapped should return unmapped employees")
    public void testGetUnmappedEmployees_Success() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("未映射员工");
        employee.setStatus(EmployeeStatus.ACTIVE);
        List<EmployeeDTO> employees = Collections.singletonList(employee);

        when(employeeService.getUnmappedEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees/unmapped"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("未映射员工"));
    }

    @Test
    @DisplayName("GET /api/employees/active should return active employees")
    public void testGetActiveEmployees_Success() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("活跃员工");
        employee.setStatus(EmployeeStatus.ACTIVE);
        List<EmployeeDTO> employees = Collections.singletonList(employee);

        when(employeeService.getActiveEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("活跃员工"));
    }

    @Test
    @DisplayName("GET /api/employees/unassigned should return unassigned employees")
    public void testGetUnassignedEmployees_Success() throws Exception {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("未分配员工");
        employee.setStatus(EmployeeStatus.ACTIVE);
        List<EmployeeDTO> employees = Collections.singletonList(employee);

        when(employeeService.getUnassignedEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees/unassigned"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("未分配员工"));
    }

    @Test
    @DisplayName("GET /api/employees/status/{status} should return employees by status")
    public void testGetEmployeesByStatus_Success() throws Exception {
        String status = "ACTIVE";
        EmployeeDTO employee = new EmployeeDTO();
        employee.setId(1L);
        employee.setName("状态员工");
        employee.setStatus(EmployeeStatus.ACTIVE);
        List<EmployeeDTO> employees = Collections.singletonList(employee);

        when(employeeService.getEmployeesByStatus(EmployeeStatus.ACTIVE)).thenReturn(employees);

        mockMvc.perform(get("/api/employees/status/{status}", status))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value("状态员工"));
    }

    @Test
    @DisplayName("POST /api/employees/{employeeId}/educations should add education successfully")
    public void testAddEducation_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeEducationDTO inputEducation = new EmployeeEducationDTO();
        inputEducation.setDegree("学士");
        inputEducation.setSchool("清华大学");

        EmployeeEducationDTO createdEducation = new EmployeeEducationDTO();
        createdEducation.setId(1L);
        createdEducation.setEmployeeId(employeeId);
        createdEducation.setDegree("学士");
        createdEducation.setSchool("清华大学");

        when(employeeEducationService.add(eq(employeeId), any(EmployeeEducationDTO.class))).thenReturn(createdEducation);

        mockMvc.perform(post("/api/employees/{employeeId}/educations", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputEducation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.degree").value("学士"))
                .andExpect(jsonPath("$.data.school").value("清华大学"));
    }

    @Test
    @DisplayName("PUT /api/employees/{employeeId}/educations/{educationId} should update education successfully")
    public void testUpdateEducation_Success() throws Exception {
        Long employeeId = 1L;
        Long educationId = 1L;
        EmployeeEducationDTO inputEducation = new EmployeeEducationDTO();
        inputEducation.setDegree("硕士");
        inputEducation.setSchool("北京大学");

        EmployeeEducationDTO updatedEducation = new EmployeeEducationDTO();
        updatedEducation.setId(educationId);
        updatedEducation.setEmployeeId(employeeId);
        updatedEducation.setDegree("硕士");
        updatedEducation.setSchool("北京大学");

        when(employeeEducationService.update(eq(employeeId), eq(educationId), any(EmployeeEducationDTO.class)))
                .thenReturn(updatedEducation);

        mockMvc.perform(put("/api/employees/{employeeId}/educations/{educationId}", employeeId, educationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputEducation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.degree").value("硕士"))
                .andExpect(jsonPath("$.data.school").value("北京大学"));
    }

    @Test
    @DisplayName("DELETE /api/employees/{employeeId}/educations/{educationId} should delete education successfully")
    public void testDeleteEducation_Success() throws Exception {
        Long employeeId = 1L;
        Long educationId = 1L;

        mockMvc.perform(delete("/api/employees/{employeeId}/educations/{educationId}", employeeId, educationId))
                .andExpect(status().isOk());

        verify(employeeEducationService).delete(employeeId, educationId);
    }

    @Test
    @DisplayName("GET /api/employees/{employeeId}/educations should return employee educations")
    public void testGetEducations_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeEducationDTO education = new EmployeeEducationDTO();
        education.setId(1L);
        education.setEmployeeId(employeeId);
        education.setDegree("学士");
        education.setSchool("清华大学");
        List<EmployeeEducationDTO> educations = Collections.singletonList(education);

        when(employeeEducationService.getByEmployeeId(employeeId)).thenReturn(educations);

        mockMvc.perform(get("/api/employees/{employeeId}/educations", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].degree").value("学士"))
                .andExpect(jsonPath("$.data[0].school").value("清华大学"));
    }

    @Test
    @DisplayName("POST /api/employees/{employeeId}/attendance should add attendance successfully")
    public void testAddAttendance_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeAttendanceDTO inputAttendance = new EmployeeAttendanceDTO(
            null, employeeId, 
            java.time.LocalDate.parse("2024-01-15"),
            java.time.LocalTime.parse("09:00"),
            java.time.LocalTime.parse("18:00"),
            "正常", "正常出勤"
        );

        EmployeeAttendanceDTO createdAttendance = new EmployeeAttendanceDTO(
            1L, employeeId,
            java.time.LocalDate.parse("2024-01-15"),
            java.time.LocalTime.parse("09:00"),
            java.time.LocalTime.parse("18:00"),
            "正常", "正常出勤"
        );

        when(employeeAttendanceServiceImpl.add(eq(employeeId), any(EmployeeAttendanceDTO.class))).thenReturn(createdAttendance);

        mockMvc.perform(post("/api/employees/{employeeId}/attendance", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputAttendance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.attendanceDate").value("2024-01-15"))
                .andExpect(jsonPath("$.data.checkInTime").value("09:00:00"));
    }

    @Test
    @DisplayName("PUT /api/employees/{employeeId}/attendance/{attendanceId} should update attendance successfully")
    public void testUpdateAttendance_Success() throws Exception {
        Long employeeId = 1L;
        Long attendanceId = 1L;
        EmployeeAttendanceDTO inputAttendance = new EmployeeAttendanceDTO(
            attendanceId, employeeId,
            java.time.LocalDate.parse("2024-01-15"),
            java.time.LocalTime.parse("08:30"),
            java.time.LocalTime.parse("17:30"),
            "正常", "正常出勤"
        );

        EmployeeAttendanceDTO updatedAttendance = new EmployeeAttendanceDTO(
            attendanceId, employeeId,
            java.time.LocalDate.parse("2024-01-15"),
            java.time.LocalTime.parse("08:30"),
            java.time.LocalTime.parse("17:30"),
            "正常", "正常出勤"
        );

        when(employeeAttendanceServiceImpl.update(eq(employeeId), eq(attendanceId), any(EmployeeAttendanceDTO.class)))
                .thenReturn(updatedAttendance);

        mockMvc.perform(put("/api/employees/{employeeId}/attendance/{attendanceId}", employeeId, attendanceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputAttendance)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.checkInTime").value("08:30:00"))
                .andExpect(jsonPath("$.data.checkOutTime").value("17:30:00"));
    }

    @Test
    @DisplayName("GET /api/employees/{employeeId}/attendance should return employee attendance")
    public void testGetAttendance_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeAttendanceDTO attendance = new EmployeeAttendanceDTO(
            1L, employeeId,
            java.time.LocalDate.parse("2024-01-15"),
            java.time.LocalTime.parse("09:00"),
            java.time.LocalTime.parse("18:00"),
            "正常", "正常出勤"
        );
        List<EmployeeAttendanceDTO> attendances = Collections.singletonList(attendance);

        when(employeeAttendanceServiceImpl.getByEmployeeId(employeeId)).thenReturn(attendances);

        mockMvc.perform(get("/api/employees/{employeeId}/attendance", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].attendanceDate").value("2024-01-15"))
                .andExpect(jsonPath("$.data[0].checkInTime").value("09:00:00"));
    }

    @Test
    @DisplayName("DELETE /api/employees/{employeeId}/attendance/{attendanceId} should delete attendance successfully")
    public void testDeleteAttendance_Success() throws Exception {
        Long employeeId = 1L;
        Long attendanceId = 1L;

        mockMvc.perform(delete("/api/employees/{employeeId}/attendance/{attendanceId}", employeeId, attendanceId))
                .andExpect(status().isOk());

        verify(employeeAttendanceServiceImpl).delete(employeeId, attendanceId);
    }

    @Test
    @DisplayName("GET /api/employees/{employeeId}/job-history should return employee job history")
    public void testGetJobHistory_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeJobHistoryDTO jobHistory = new EmployeeJobHistoryDTO();
        jobHistory.setId(1L);
        jobHistory.setEmployeeId(employeeId);
        jobHistory.setPosition("软件工程师");
        jobHistory.setDepartment("技术部");
        List<EmployeeJobHistoryDTO> jobHistories = Collections.singletonList(jobHistory);

        when(employeeJobHistoryService.getByEmployeeId(employeeId)).thenReturn(jobHistories);

        mockMvc.perform(get("/api/employees/{employeeId}/job-history", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].position").value("软件工程师"))
                .andExpect(jsonPath("$.data[0].department").value("技术部"));
    }

    @Test
    @DisplayName("POST /api/employees/{employeeId}/job-history should create job history successfully")
    public void testCreateJobHistory_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeJobHistoryDTO inputJobHistory = new EmployeeJobHistoryDTO();
        inputJobHistory.setPosition("高级工程师");
        inputJobHistory.setDepartment("技术部");

        EmployeeJobHistoryDTO createdJobHistory = new EmployeeJobHistoryDTO();
        createdJobHistory.setId(1L);
        createdJobHistory.setEmployeeId(employeeId);
        createdJobHistory.setPosition("高级工程师");
        createdJobHistory.setDepartment("技术部");

        when(employeeJobHistoryService.add(eq(employeeId), any(EmployeeJobHistoryDTO.class))).thenReturn(createdJobHistory);

        mockMvc.perform(post("/api/employees/{employeeId}/job-history", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputJobHistory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.position").value("高级工程师"));
    }

    @Test
    @DisplayName("PUT /api/employees/{employeeId}/job-history/{id} should update job history successfully")
    public void testUpdateJobHistory_Success() throws Exception {
        Long employeeId = 1L;
        Long jobHistoryId = 1L;
        EmployeeJobHistoryDTO inputJobHistory = new EmployeeJobHistoryDTO();
        inputJobHistory.setPosition("技术经理");
        inputJobHistory.setDepartment("技术部");

        EmployeeJobHistoryDTO updatedJobHistory = new EmployeeJobHistoryDTO();
        updatedJobHistory.setId(jobHistoryId);
        updatedJobHistory.setEmployeeId(employeeId);
        updatedJobHistory.setPosition("技术经理");
        updatedJobHistory.setDepartment("技术部");

        when(employeeJobHistoryService.update(eq(employeeId), eq(jobHistoryId), any(EmployeeJobHistoryDTO.class)))
                .thenReturn(updatedJobHistory);

        mockMvc.perform(put("/api/employees/{employeeId}/job-history/{id}", employeeId, jobHistoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputJobHistory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.position").value("技术经理"));
    }

    @Test
    @DisplayName("DELETE /api/employees/{employeeId}/job-history/{id} should delete job history successfully")
    public void testDeleteJobHistory_Success() throws Exception {
        Long employeeId = 1L;
        Long jobHistoryId = 1L;

        mockMvc.perform(delete("/api/employees/{employeeId}/job-history/{id}", employeeId, jobHistoryId))
                .andExpect(status().isOk());

        verify(employeeJobHistoryService).delete(employeeId, jobHistoryId);
    }

    @Test
    @DisplayName("POST /api/employees/{employeeId}/leaves should add leave successfully")
    public void testAddLeave_Success() throws Exception {
        Long employeeId = 1L;
        EmployeeLeaveDTO inputLeave = new EmployeeLeaveDTO();
        inputLeave.setType("年假");
        inputLeave.setStartTime(java.time.LocalDate.parse("2024-02-01"));
        inputLeave.setEndTime(java.time.LocalDate.parse("2024-02-03"));

        EmployeeLeaveDTO createdLeave = new EmployeeLeaveDTO();
        createdLeave.setId(1L);
        createdLeave.setEmployeeId(employeeId);
        createdLeave.setType("年假");
        createdLeave.setStartTime(java.time.LocalDate.parse("2024-02-01"));
        createdLeave.setEndTime(java.time.LocalDate.parse("2024-02-03"));

        when(employeeLeaveService.add(eq(employeeId), any(EmployeeLeaveDTO.class))).thenReturn(createdLeave);

        mockMvc.perform(post("/api/employees/{employeeId}/leaves", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputLeave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.type").value("年假"));
    }

    @Test
    @DisplayName("PUT /api/employees/{employeeId}/leaves/{leaveId} should update leave successfully")
    public void testUpdateLeave_Success() throws Exception {
        Long employeeId = 1L;
        Long leaveId = 1L;
        EmployeeLeaveDTO inputLeave = new EmployeeLeaveDTO();
        inputLeave.setType("病假");
        inputLeave.setStartTime(java.time.LocalDate.parse("2024-02-05"));
        inputLeave.setEndTime(java.time.LocalDate.parse("2024-02-07"));

        EmployeeLeaveDTO updatedLeave = new EmployeeLeaveDTO();
        updatedLeave.setId(leaveId);
        updatedLeave.setEmployeeId(employeeId);
        updatedLeave.setType("病假");
        updatedLeave.setStartTime(java.time.LocalDate.parse("2024-02-05"));
        updatedLeave.setEndTime(java.time.LocalDate.parse("2024-02-07"));

        when(employeeLeaveService.update(eq(employeeId), eq(leaveId), any(EmployeeLeaveDTO.class)))
                .thenReturn(updatedLeave);

        mockMvc.perform(put("/api/employees/{employeeId}/leaves/{leaveId}", employeeId, leaveId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputLeave)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.type").value("病假"));
    }

    @Test
    @DisplayName("DELETE /api/employees/{employeeId}/leaves/{leaveId} should delete leave successfully")
    public void testDeleteLeave_Success() throws Exception {
        Long employeeId = 1L;
        Long leaveId = 1L;

        mockMvc.perform(delete("/api/employees/{employeeId}/leaves/{leaveId}", employeeId, leaveId))
                .andExpect(status().isOk());

        verify(employeeLeaveService).delete(employeeId, leaveId);
    }

    @Test
    @DisplayName("POST /api/employees/{employeeId}/payrolls should add payroll successfully")
    public void testAddPayroll_Success() throws Exception {
        Long employeeId = 1L;
        EmployeePayrollDTO inputPayroll = new EmployeePayrollDTO();
        inputPayroll.setPayPeriodStart(java.time.LocalDateTime.parse("2024-01-01T00:00:00"));
        inputPayroll.setPayPeriodEnd(java.time.LocalDateTime.parse("2024-01-31T23:59:59"));
        inputPayroll.setBasicSalary(new java.math.BigDecimal("10000.00"));
        inputPayroll.setBonus(new java.math.BigDecimal("2000.00"));

        EmployeePayrollDTO createdPayroll = new EmployeePayrollDTO();
        createdPayroll.setId(1L);
        createdPayroll.setEmployeeId(employeeId);
        createdPayroll.setPayPeriodStart(java.time.LocalDateTime.parse("2024-01-01T00:00:00"));
        createdPayroll.setPayPeriodEnd(java.time.LocalDateTime.parse("2024-01-31T23:59:59"));
        createdPayroll.setBasicSalary(new java.math.BigDecimal("10000.00"));
        createdPayroll.setBonus(new java.math.BigDecimal("2000.00"));

        when(employeePayrollService.add(eq(employeeId), any(EmployeePayrollDTO.class))).thenReturn(createdPayroll);

        mockMvc.perform(post("/api/employees/{employeeId}/payrolls", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputPayroll)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.basicSalary").value(10000.00));
    }

    @Test
    @DisplayName("PUT /api/employees/{employeeId}/payrolls/{payrollId} should update payroll successfully")
    public void testUpdatePayroll_Success() throws Exception {
        Long employeeId = 1L;
        Long payrollId = 1L;
        EmployeePayrollDTO inputPayroll = new EmployeePayrollDTO();
        inputPayroll.setPayPeriodStart(java.time.LocalDateTime.parse("2024-01-01T00:00:00"));
        inputPayroll.setPayPeriodEnd(java.time.LocalDateTime.parse("2024-01-31T23:59:59"));
        inputPayroll.setBasicSalary(new java.math.BigDecimal("12000.00"));
        inputPayroll.setBonus(new java.math.BigDecimal("3000.00"));

        EmployeePayrollDTO updatedPayroll = new EmployeePayrollDTO();
        updatedPayroll.setId(payrollId);
        updatedPayroll.setEmployeeId(employeeId);
        updatedPayroll.setPayPeriodStart(java.time.LocalDateTime.parse("2024-01-01T00:00:00"));
        updatedPayroll.setPayPeriodEnd(java.time.LocalDateTime.parse("2024-01-31T23:59:59"));
        updatedPayroll.setBasicSalary(new java.math.BigDecimal("12000.00"));
        updatedPayroll.setBonus(new java.math.BigDecimal("3000.00"));

        when(employeePayrollService.update(eq(employeeId), eq(payrollId), any(EmployeePayrollDTO.class)))
                .thenReturn(updatedPayroll);

        mockMvc.perform(put("/api/employees/{employeeId}/payrolls/{payrollId}", employeeId, payrollId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputPayroll)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.basicSalary").value(12000.00))
                .andExpect(jsonPath("$.data.bonus").value(3000.00));
    }

    @Test
    @DisplayName("DELETE /api/employees/{employeeId}/payrolls/{payrollId} should delete payroll successfully")
    public void testDeletePayroll_Success() throws Exception {
        Long employeeId = 1L;
        Long payrollId = 1L;

        mockMvc.perform(delete("/api/employees/{employeeId}/payrolls/{payrollId}", employeeId, payrollId))
                .andExpect(status().isOk());

        verify(employeePayrollService).delete(employeeId, payrollId);
    }
} 