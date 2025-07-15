package com.fwai.turtle.modules.finance.controller;

import com.fwai.turtle.modules.finance.dto.ReimbursementDTO;
import com.fwai.turtle.modules.finance.service.ReimbursementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reimbursements")
@RequiredArgsConstructor
@Tag(name = "Reimbursement", description = "Reimbursement management APIs")
public class ReimbursementController {
    private final ReimbursementService reimbursementService;

    @GetMapping
    @Operation(summary = "Get all reimbursements with pagination")
    public ResponseEntity<Page<ReimbursementDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(reimbursementService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reimbursement by ID")
    public ResponseEntity<ReimbursementDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reimbursementService.getById(id));
    }

    @GetMapping("/no/{reimbursementNo}")
    @Operation(summary = "Get reimbursement by reimbursement number")
    public ResponseEntity<ReimbursementDTO> getByReimbursementNo(@PathVariable String reimbursementNo) {
        return ResponseEntity.ok(reimbursementService.getByReimbursementNo(reimbursementNo));
    }

    @PostMapping
    @Operation(summary = "Create a new reimbursement draft")
    public ResponseEntity<ReimbursementDTO> create(@RequestBody ReimbursementDTO reimbursementDTO) {
        return new ResponseEntity<>(reimbursementService.create(reimbursementDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/apply")
    @Operation(summary = "Apply for a reimbursement")
    public ResponseEntity<ReimbursementDTO> apply(@PathVariable Long id) {
        return ResponseEntity.ok(reimbursementService.apply(id));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a reimbursement")
    public ResponseEntity<ReimbursementDTO> approve(@PathVariable Long id) {
        return ResponseEntity.ok(reimbursementService.approve(id));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a reimbursement")
    public ResponseEntity<ReimbursementDTO> reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(reimbursementService.reject(id, reason));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a draft reimbursement")
    public ResponseEntity<ReimbursementDTO> update(@PathVariable Long id, @RequestBody ReimbursementDTO reimbursementDTO) {
        return ResponseEntity.ok(reimbursementService.update(id, reimbursementDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reimbursement")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reimbursementService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applicant/{applicantId}")
    @Operation(summary = "Get reimbursements by applicant ID")
    public ResponseEntity<List<ReimbursementDTO>> getByApplicant(@PathVariable Long applicantId) {
        return ResponseEntity.ok(reimbursementService.getByApplicant(applicantId));
    }

    @GetMapping("/approver/{approverId}")
    @Operation(summary = "Get reimbursements by approver ID")
    public ResponseEntity<List<ReimbursementDTO>> getByApprover(@PathVariable Long approverId) {
        return ResponseEntity.ok(reimbursementService.getByApprover(approverId));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get reimbursements by project ID")
    public ResponseEntity<List<ReimbursementDTO>> getByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(reimbursementService.getByProject(projectId));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get reimbursements by date range")
    public ResponseEntity<Page<ReimbursementDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        return ResponseEntity.ok(reimbursementService.getByDateRange(startDate, endDate, pageable));
    }
}
