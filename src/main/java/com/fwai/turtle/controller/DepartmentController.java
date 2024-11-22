package com.fwai.turtle.controller;

import com.fwai.turtle.common.Result;
import com.fwai.turtle.dto.DepartmentDTO;
import com.fwai.turtle.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping
    public ResponseEntity<Result<Page<DepartmentDTO>>> findAll(Pageable pageable) {
        Page<DepartmentDTO> departments = departmentService.findAll(pageable);
        return ResponseEntity.ok(Result.success(departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<DepartmentDTO>> findById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.findById(id);
        return ResponseEntity.ok(Result.success(department));
    }

    @PostMapping
    public ResponseEntity<Result<DepartmentDTO>> create(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO created = departmentService.create(departmentDTO);
        return ResponseEntity.ok(Result.success(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Result<DepartmentDTO>> update(
            @PathVariable Long id,
            @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updated = departmentService.update(id, departmentDTO);
        return ResponseEntity.ok(Result.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.ok(Result.success(null));
    }

    @GetMapping("/search")
    public ResponseEntity<Result<Page<DepartmentDTO>>> search(
            @RequestParam String query,
            Pageable pageable) {
        Page<DepartmentDTO> departments = departmentService.search(query, pageable);
        return ResponseEntity.ok(Result.success(departments));
    }
}
