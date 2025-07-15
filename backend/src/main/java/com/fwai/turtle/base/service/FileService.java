package com.fwai.turtle.base.service;

import com.fwai.turtle.base.dto.FileDTO;
import com.fwai.turtle.base.types.ClientType;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface FileService {
    FileDTO upload(MultipartFile file, ClientType clientType, Long clientId);
    List<FileDTO> findByClient(ClientType clientType, Long clientId);
    void delete(Long id);
    byte[] download(Long id);
}
