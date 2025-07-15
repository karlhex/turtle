package com.fwai.turtle.base.entity;

import com.fwai.turtle.base.entity.BaseEntity;
import com.fwai.turtle.base.types.ClientType;
import com.fwai.turtle.base.types.FileType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "t_file")
@EqualsAndHashCode(callSuper = true)
public class File extends BaseEntity {

    @Column(name = "client_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "file_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FileType fileType;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "storage_name", nullable = false)
    private String storageName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;
}
