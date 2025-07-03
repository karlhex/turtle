package com.fwai.turtle.persistence.mapper;

import com.fwai.turtle.dto.FileDTO;
import com.fwai.turtle.persistence.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {
    
    FileDTO toDTO(File entity);
    File toEntity(FileDTO dto);
}
