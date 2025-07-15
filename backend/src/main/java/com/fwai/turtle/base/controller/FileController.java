package com.fwai.turtle.base.controller;

import com.fwai.turtle.base.dto.FileDTO;
import com.fwai.turtle.base.service.FileService;
 import com.fwai.turtle.base.types.ClientType;
import com.fwai.turtle.base.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ApiResponse<FileDTO> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("clientType") ClientType clientType,
            @RequestParam("clientId") Long clientId) {
        return ApiResponse.ok(fileService.upload(file, clientType, clientId));
    }

    @GetMapping
    public ApiResponse<List<FileDTO>> findByClient(
            @RequestParam("clientType") ClientType clientType,
            @RequestParam("clientId") Long clientId) {
        return ApiResponse.ok(fileService.findByClient(clientType, clientId));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        fileService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        FileDTO file = fileService.findByClient(ClientType.CONTRACT, id).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("File not found"));
        
        byte[] data = fileService.download(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType(file.getMimeType()))
                .contentLength(file.getFileSize())
                .body(resource);
    }
}
