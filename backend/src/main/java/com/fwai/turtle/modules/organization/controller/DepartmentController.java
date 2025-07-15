package com.fwai.turtle.modules.organization.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.organization.dto.DepartmentDTO;
import com.fwai.turtle.modules.organization.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DepartmentDTO>>> findAll(Pageable pageable) {
        Page<DepartmentDTO> departments = departmentService.findAll(pageable);
        return ResponseEntity.ok(ApiResponse.ok(departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> findById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(department));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentDTO>> create(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO created = departmentService.create(departmentDTO);
        return ResponseEntity.ok(ApiResponse.ok(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDTO>> update(
            @PathVariable Long id,
            @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updated = departmentService.update(id, departmentDTO);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<DepartmentDTO>>> search(
            @RequestParam String query,
            Pageable pageable) {
        Page<DepartmentDTO> departments = departmentService.search(query, pageable);
        return ResponseEntity.ok(ApiResponse.ok(departments));
    }
}
