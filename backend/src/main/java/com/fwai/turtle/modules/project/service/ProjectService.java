package com.fwai.turtle.modules.project.service;

import com.fwai.turtle.modules.project.dto.ProjectDTO;
import com.fwai.turtle.base.types.ProjectStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {
    ProjectDTO create(ProjectDTO projectDTO);
    ProjectDTO update(Long id, ProjectDTO projectDTO);
    ProjectDTO getById(Long id);
    ProjectDTO getByProjectNo(String projectNo);
    Page<ProjectDTO> search(String projectName, Pageable pageable);
    void delete(Long id);
    ProjectDTO updateStatus(Long id, ProjectStatus newStatus);
    Page<ProjectDTO> findAll(Pageable pageable);
}
