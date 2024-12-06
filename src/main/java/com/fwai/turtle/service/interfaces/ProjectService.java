package com.fwai.turtle.service.interfaces;

import com.fwai.turtle.dto.ProjectDTO;
import com.fwai.turtle.types.ProjectStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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
