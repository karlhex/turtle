package com.fwai.turtle.base.dto;

import com.fwai.turtle.base.types.ClientType;
import com.fwai.turtle.base.types.FileType;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FileDTO {
    private Long id;
    private ClientType clientType;
    private Long clientId;
    private FileType fileType;
    private String originalName;
    private String storageName;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
