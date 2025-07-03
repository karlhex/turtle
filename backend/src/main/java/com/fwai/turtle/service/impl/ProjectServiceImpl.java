package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.ProjectDTO;
import com.fwai.turtle.exception.DuplicateRecordException;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.Employee;
import com.fwai.turtle.persistence.entity.Project;
import com.fwai.turtle.persistence.mapper.ProjectMapper;
import com.fwai.turtle.persistence.repository.EmployeeRepository;
import com.fwai.turtle.persistence.repository.ProjectRepository;
import com.fwai.turtle.service.interfaces.ProjectService;
import com.fwai.turtle.types.ProjectStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author fwai
 * @since 2022/1/11
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectDTO create(ProjectDTO projectDTO) {
        // 检查项目编号是否已存在
        if (projectRepository.existsByProjectNo(projectDTO.getProjectNo())) {
            throw new DuplicateRecordException("项目编号已存在: " + projectDTO.getProjectNo());
        }

        Project project = projectMapper.toEntity(projectDTO);
        
        // 设置项目负责人
        Employee manager = employeeRepository.findById(projectDTO.getManager().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", projectDTO.getManager().getId()));
        project.setManager(manager);

        project = projectRepository.save(project);
        return projectMapper.toDTO(project);
    }

    @Override
    public ProjectDTO update(Long id, ProjectDTO projectDTO) {
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        // 检查项目编号是否被其他项目使用
        if (!existingProject.getProjectNo().equals(projectDTO.getProjectNo()) &&
            projectRepository.existsByProjectNo(projectDTO.getProjectNo())) {
            throw new DuplicateRecordException("项目编号已存在: " + projectDTO.getProjectNo());
        }

        projectMapper.updateEntity(projectDTO, existingProject);

        // 更新项目负责人
        if (projectDTO.getManager() != null) {
            Employee manager = employeeRepository.findById(projectDTO.getManager().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", projectDTO.getManager().getId()));
            existingProject.setManager(manager);
        }

        existingProject = projectRepository.save(existingProject);
        return projectMapper.toDTO(existingProject);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        return projectMapper.toDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getByProjectNo(String projectNo) {
        Project project = projectRepository.findByProjectNo(projectNo)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectNo", projectNo));
        return projectMapper.toDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> search(String projectName, Pageable pageable) {
        return projectRepository.findByProjectNameContaining(projectName, pageable)
                .map(projectMapper::toDTO);
    }

    @Override
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project", "id", id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public ProjectDTO updateStatus(Long id, ProjectStatus newStatus) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
        project.setStatus(newStatus);
        project = projectRepository.save(project);
        return projectMapper.toDTO(project);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(projectMapper::toDTO);
    }
}
