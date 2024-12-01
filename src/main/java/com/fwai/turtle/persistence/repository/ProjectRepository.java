package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectNo(String projectNo);
    boolean existsByProjectNo(String projectNo);
    Page<Project> findByProjectNameContaining(String projectName, Pageable pageable);
}
