package com.fwai.turtle.base.service.impl;

import com.fwai.turtle.base.dto.FileDTO;
import com.fwai.turtle.base.entity.File;
import com.fwai.turtle.base.mapper.FileMapper;
import com.fwai.turtle.base.repository.FileRepository;
import com.fwai.turtle.base.service.FileService;
import com.fwai.turtle.base.types.ClientType;
import com.fwai.turtle.base.types.FileType;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    @Transactional
    public FileDTO upload(MultipartFile file, ClientType clientType, Long clientId) {
        try {
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                throw new IllegalArgumentException("File must have a valid filename");
            }
                        
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String storageName = UUID.randomUUID().toString() + extension;
            
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(storageName);
            Files.copy(file.getInputStream(), filePath);

            File fileEntity = new File();
            fileEntity.setClientType(clientType);
            fileEntity.setClientId(clientId);
            fileEntity.setFileType(determineFileType(file.getContentType()));
            fileEntity.setOriginalName(originalFilename);
            fileEntity.setStorageName(storageName);
            fileEntity.setFileSize(file.getSize());
            fileEntity.setMimeType(file.getContentType());
            fileEntity.setUploadTime(LocalDateTime.now());

            return fileMapper.toDTO(fileRepository.save(fileEntity));
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileDTO> findByClient(ClientType clientType, Long clientId) {
        return fileRepository.findByClientTypeAndClientId(clientType, clientId)
                .stream()
                .map(fileMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        
        try {
            Path filePath = Paths.get(uploadDir, file.getStorageName());
            Files.deleteIfExists(filePath);
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] download(Long id) {
        File file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));
        
        try {
            Path filePath = Paths.get(uploadDir, file.getStorageName());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private FileType determineFileType(String mimeType) {
        if (mimeType == null) return FileType.OTHER;
        
        if (mimeType.startsWith("image/")) return FileType.IMAGE;
        if (mimeType.startsWith("video/")) return FileType.VIDEO;
        if (mimeType.startsWith("audio/")) return FileType.AUDIO;
        if (mimeType.startsWith("application/") || mimeType.startsWith("text/")) return FileType.DOCUMENT;
        
        return FileType.OTHER;
    }
}
