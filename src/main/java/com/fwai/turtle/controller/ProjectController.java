package com.fwai.turtle.controller;

import com.fwai.turtle.common.ApiResponse;
import com.fwai.turtle.dto.ProjectDTO;
import com.fwai.turtle.persistence.entity.ProjectStatus;
import com.fwai.turtle.service.interfaces.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ApiResponse<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        return ApiResponse.ok(projectService.create(projectDTO));
    }

    @PutMapping("/{id}")
    public ApiResponse<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        return ApiResponse.ok(projectService.update(id, projectDTO));
    }

    @GetMapping("/{id}")
    public ApiResponse<ProjectDTO> getProject(@PathVariable Long id) {
        return ApiResponse.ok(projectService.getById(id));
    }

    @GetMapping("/no/{projectNo}")
    public ApiResponse<ProjectDTO> getProjectByNo(@PathVariable String projectNo) {
        return ApiResponse.ok(projectService.getByProjectNo(projectNo));
    }

    @GetMapping("/search")
    public ApiResponse<Page<ProjectDTO>> searchProjects(
            @RequestParam(required = false, defaultValue = "") String projectName,
            Pageable pageable) {
        return ApiResponse.ok(projectService.search(projectName, pageable));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return ApiResponse.ok(null);
    }

    @PutMapping("/{id}/status")
    public ApiResponse<ProjectDTO> updateProjectStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {
        return ApiResponse.ok(projectService.updateStatus(id, status));
    }
}
