package com.fwai.turtle.modules.organization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.organization.dto.DepartmentDTO;
import com.fwai.turtle.modules.organization.service.DepartmentService;
import com.fwai.turtle.security.service.JwtTokenService;
import com.fwai.turtle.security.service.TokenBlacklistService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private JwtTokenService jwtTokenService;
    @MockBean
    private TokenBlacklistService tokenBlacklistService;

    @Test
    @DisplayName("GET /api/departments should return paged departments")
    void testFindAll() throws Exception {
        DepartmentDTO dto = new DepartmentDTO(1L, "研发部", "负责产品研发", "RD001", true, null, null);
        Page<DepartmentDTO> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);
        when(departmentService.findAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/api/departments").with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].name").value("研发部"));
    }

    @Test
    @DisplayName("GET /api/departments/{id} should return department by id")
    void testFindById() throws Exception {
        DepartmentDTO dto = new DepartmentDTO(1L, "研发部", "负责产品研发", "RD001", true, null, null);
        when(departmentService.findById(1L)).thenReturn(dto);
        mockMvc.perform(get("/api/departments/1").with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("研发部"));
    }

    @Test
    @DisplayName("POST /api/departments should create department")
    void testCreate() throws Exception {
        DepartmentDTO input = new DepartmentDTO(null, "新部门", "新部门描述", "NEW001", true, null, null);
        DepartmentDTO created = new DepartmentDTO(2L, "新部门", "新部门描述", "NEW001", true, null, null);
        when(departmentService.create(any(DepartmentDTO.class))).thenReturn(created);
        mockMvc.perform(post("/api/departments")
                .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(2L))
                .andExpect(jsonPath("$.data.name").value("新部门"));
    }

    @Test
    @DisplayName("PUT /api/departments/{id} should update department")
    void testUpdate() throws Exception {
        DepartmentDTO input = new DepartmentDTO(null, "更新部门", "更新部门描述", "UPDATE001", true, null, null);
        DepartmentDTO updated = new DepartmentDTO(1L, "更新部门", "更新部门描述", "UPDATE001", true, null, null);
        when(departmentService.update(eq(1L), any(DepartmentDTO.class))).thenReturn(updated);
        mockMvc.perform(put("/api/departments/1")
                .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1L))
                .andExpect(jsonPath("$.data.name").value("更新部门"));
    }

    @Test
    @DisplayName("DELETE /api/departments/{id} should delete department")
    void testDelete() throws Exception {
        doNothing().when(departmentService).delete(1L);
        mockMvc.perform(delete("/api/departments/1")
                .with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN"))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("GET /api/departments/search should search departments")
    void testSearch() throws Exception {
        DepartmentDTO dto = new DepartmentDTO(1L, "研发部", "负责产品研发", "RD001", true, null, null);
        Page<DepartmentDTO> page = new PageImpl<>(Collections.singletonList(dto), PageRequest.of(0, 10), 1);
        when(departmentService.search(eq("研"), any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/api/departments/search").param("query", "研").with(SecurityMockMvcRequestPostProcessors.user("test").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content[0].id").value(1L))
                .andExpect(jsonPath("$.data.content[0].name").value("研发部"));
    }
} 