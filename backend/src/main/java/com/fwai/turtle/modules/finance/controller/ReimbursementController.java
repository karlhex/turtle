package com.fwai.turtle.modules.finance.controller;

import com.fwai.turtle.base.dto.ApiResponse;
import com.fwai.turtle.modules.finance.dto.ReimbursementDTO;
import com.fwai.turtle.modules.finance.service.ReimbursementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ApiResponse<Page<ReimbursementDTO>> findAll(Pageable pageable) {
        return ApiResponse.ok(reimbursementService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reimbursement by ID")
    public ApiResponse<ReimbursementDTO> getById(@PathVariable Long id) {
        return ApiResponse.ok(reimbursementService.getById(id));
    }

    @GetMapping("/no/{reimbursementNo}")
    @Operation(summary = "Get reimbursement by reimbursement number")
    public ApiResponse<ReimbursementDTO> getByReimbursementNo(@PathVariable String reimbursementNo) {
        return ApiResponse.ok(reimbursementService.getByReimbursementNo(reimbursementNo));
    }

    @PostMapping
    @Operation(summary = "Create a new reimbursement draft")
    public ApiResponse<ReimbursementDTO> create(@RequestBody ReimbursementDTO reimbursementDTO) {
        return ApiResponse.ok(reimbursementService.create(reimbursementDTO));
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit reimbursement for approval")
    public ApiResponse<ReimbursementDTO> submitForApproval(@PathVariable Long id) {
        return ApiResponse.ok(reimbursementService.submitForApproval(id));
    }

    @PostMapping("/{id}/apply")
    @Operation(summary = "Apply for a reimbursement (legacy)")
    public ApiResponse<ReimbursementDTO> apply(@PathVariable Long id) {
        return ApiResponse.ok(reimbursementService.apply(id));
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve a reimbursement")
    public ApiResponse<ReimbursementDTO> approve(@PathVariable Long id) {
        return ApiResponse.ok(reimbursementService.approve(id));
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a reimbursement")
    public ApiResponse<ReimbursementDTO> reject(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return ApiResponse.ok(reimbursementService.reject(id, reason != null ? reason : "No reason provided"));
    }

    @PostMapping("/{id}/resubmit")
    @Operation(summary = "Resubmit a rejected reimbursement")
    public ApiResponse<ReimbursementDTO> resubmit(@PathVariable Long id) {
        return ApiResponse.ok(reimbursementService.resubmit(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a draft reimbursement")
    public ApiResponse<ReimbursementDTO> update(@PathVariable Long id, @RequestBody ReimbursementDTO reimbursementDTO) {
        return ApiResponse.ok(reimbursementService.update(id, reimbursementDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a reimbursement")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        reimbursementService.delete(id);
        return ApiResponse.ok(null);
    }

    @GetMapping("/applicant/{applicantId}")
    @Operation(summary = "Get reimbursements by applicant ID")
    public ApiResponse<List<ReimbursementDTO>> getByApplicant(@PathVariable Long applicantId) {
        return ApiResponse.ok(reimbursementService.getByApplicant(applicantId));
    }

    @GetMapping("/approver/{approverId}")
    @Operation(summary = "Get reimbursements by approver ID")
    public ApiResponse<List<ReimbursementDTO>> getByApprover(@PathVariable Long approverId) {
        return ApiResponse.ok(reimbursementService.getByApprover(approverId));
    }

    @GetMapping("/project/{projectId}")
    @Operation(summary = "Get reimbursements by project ID")
    public ApiResponse<List<ReimbursementDTO>> getByProject(@PathVariable Long projectId) {
        return ApiResponse.ok(reimbursementService.getByProject(projectId));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get reimbursements by date range")
    public ApiResponse<Page<ReimbursementDTO>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Pageable pageable) {
        return ApiResponse.ok(reimbursementService.getByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get reimbursements by status")
    public ApiResponse<List<ReimbursementDTO>> getByStatus(@PathVariable String status) {
        // This would need to be implemented in the service layer
        return ApiResponse.ok(List.of()); // TODO: Implement status-based filtering
    }

    @GetMapping("/pending")
    @Operation(summary = "Get all pending reimbursements")
    public ApiResponse<List<ReimbursementDTO>> getPending() {
        // This would filter for PENDING status reimbursements
        return ApiResponse.ok(List.of()); // TODO: Implement pending reimbursements filter
    }

}
