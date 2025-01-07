package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.File;
import com.fwai.turtle.types.ClientType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByClientTypeAndClientId(ClientType clientType, Long clientId);
}
