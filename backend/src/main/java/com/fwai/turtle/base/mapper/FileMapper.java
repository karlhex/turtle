package com.fwai.turtle.base.mapper;

import com.fwai.turtle.base.dto.FileDTO;
import com.fwai.turtle.base.entity.File;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {
    
    FileDTO toDTO(File entity);
    File toEntity(FileDTO dto);
}
