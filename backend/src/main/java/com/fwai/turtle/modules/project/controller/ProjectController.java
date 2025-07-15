package com.fwai.turtle.modules.project.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.project.dto.ProjectDTO;
import com.fwai.turtle.modules.project.service.ProjectService;
import com.fwai.turtle.base.types.ProjectStatus;

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

    @GetMapping
    public ApiResponse<Page<ProjectDTO>> getAllProjects(Pageable pageable) {
        return ApiResponse.ok(projectService.findAll(pageable));
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
