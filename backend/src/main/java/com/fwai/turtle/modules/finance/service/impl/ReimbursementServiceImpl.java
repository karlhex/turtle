package com.fwai.turtle.modules.finance.service.impl;

import com.fwai.turtle.modules.finance.dto.ReimbursementDTO;
import com.fwai.turtle.modules.finance.dto.ReimbursementItemDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.finance.entity.Reimbursement;
import com.fwai.turtle.modules.finance.entity.ReimbursementItem;
import com.fwai.turtle.modules.finance.service.ReimbursementService;
import com.fwai.turtle.modules.finance.repository.ReimbursementRepository;
import com.fwai.turtle.modules.organization.repository.EmployeeRepository;
import com.fwai.turtle.modules.project.repository.ProjectRepository;
import com.fwai.turtle.modules.finance.mapper.ReimbursementItemMapper;
import com.fwai.turtle.modules.finance.mapper.ReimbursementMapper;
import com.fwai.turtle.base.types.ReimbursementStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReimbursementServiceImpl implements ReimbursementService {
    private final ReimbursementRepository reimbursementRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final ReimbursementMapper reimbursementMapper;
    private final ReimbursementItemMapper itemMapper;

    @Override
    public Page<ReimbursementDTO> findAll(Pageable pageable) {
        return reimbursementRepository.findAll(pageable)
                .map(reimbursementMapper::toDTO);
    }

    @Override
    public ReimbursementDTO getById(Long id) {
        return reimbursementRepository.findById(id)
                .map(reimbursementMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", id));
    }

    @Override
    public ReimbursementDTO getByReimbursementNo(String reimbursementNo) {
        return reimbursementRepository.findByReimbursementNo(reimbursementNo)
                .map(reimbursementMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "reimbursementNo", reimbursementNo));
    }

    @Override
    public ReimbursementDTO create(ReimbursementDTO reimbursementDTO) {
        // Validate references
        validateReferences(reimbursementDTO);
        
        // Convert DTO to entity and save
        Reimbursement reimbursement = reimbursementMapper.toEntity(reimbursementDTO);
        
        // Set initial status to DRAFT
        reimbursement.setStatus(ReimbursementStatus.DRAFT);
        
        // Set up bidirectional relationship for items
        if (reimbursement.getItems() != null) {
            reimbursement.getItems().forEach(item -> item.setReimbursement(reimbursement));
        }
        
        return reimbursementMapper.toDTO(reimbursementRepository.save(reimbursement));
    }

    @Override
    public ReimbursementDTO update(Long id, ReimbursementDTO reimbursementDTO) {
        // Validate references
        validateReferences(reimbursementDTO);
        
        // Find existing reimbursement
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", id));
        
        // Only allow updates if status is DRAFT
        if (!ReimbursementStatus.DRAFT.equals(reimbursement.getStatus())) {
            throw new IllegalStateException("Can only update reimbursements in DRAFT status");
        }
        
        // Clear existing items
        reimbursement.getItems().clear();
        
        // Convert DTO to entity and update
        Reimbursement updatedReimbursement = reimbursementMapper.toEntity(reimbursementDTO);
        updatedReimbursement.setId(id);
        updatedReimbursement.setStatus(ReimbursementStatus.DRAFT); // Maintain DRAFT status
        
        // Set up bidirectional relationship for new items
        if (updatedReimbursement.getItems() != null) {
            updatedReimbursement.getItems().forEach(item -> item.setReimbursement(updatedReimbursement));
        }
        
        return reimbursementMapper.toDTO(reimbursementRepository.save(updatedReimbursement));
    }

    @Override
    public void delete(Long id) {
        if (!reimbursementRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reimbursement", "id", id);
        }
        reimbursementRepository.deleteById(id);
    }

    @Override
    public List<ReimbursementDTO> getByApplicant(Long applicantId) {
        return reimbursementRepository.findByApplicantId(applicantId)
                .stream()
                .map(reimbursementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReimbursementDTO> getByApprover(Long approverId) {
        return reimbursementRepository.findByApproverId(approverId)
                .stream()
                .map(reimbursementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReimbursementDTO> getByProject(Long projectId) {
        return reimbursementRepository.findByProjectId(projectId)
                .stream()
                .map(reimbursementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReimbursementDTO> getByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return reimbursementRepository.findByApplicationDateBetween(startDate, endDate, pageable)
                .map(reimbursementMapper::toDTO);
    }

    private void validateReferences(ReimbursementDTO dto) {
        // Validate applicant exists
        if (!employeeRepository.existsById(dto.getApplicantId())) {
            throw new ResourceNotFoundException("Employee", "id", dto.getApplicantId());
        }

        // Validate approver exists if provided
        if (dto.getApproverId() != null && !employeeRepository.existsById(dto.getApproverId())) {
            throw new ResourceNotFoundException("Employee", "id", dto.getApproverId());
        }

        // Validate project exists if provided
        if (dto.getProjectId() != null && !projectRepository.existsById(dto.getProjectId())) {
            throw new ResourceNotFoundException("Project", "id", dto.getProjectId());
        }
    }

    @Override
    public ReimbursementDTO addItem(Long reimbursementId, ReimbursementItemDTO itemDTO) {
        Reimbursement reimbursement = reimbursementRepository.findById(reimbursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", reimbursementId));

        ReimbursementItem item = itemMapper.toEntity(itemDTO);
        item.setReimbursement(reimbursement);
        reimbursement.getItems().add(item);

        Reimbursement updatedReimbursement = reimbursementRepository.save(reimbursement);
        return reimbursementMapper.toDTO(updatedReimbursement);
    }

    @Override
    public ReimbursementDTO updateItem(Long reimbursementId, Long itemId, ReimbursementItemDTO itemDTO) {
        Reimbursement reimbursement = reimbursementRepository.findById(reimbursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", reimbursementId));

        ReimbursementItem existingItem = reimbursement.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("ReimbursementItem", "id", itemId));

        itemMapper.updateEntityFromDTO(itemDTO, existingItem);
        Reimbursement updatedReimbursement = reimbursementRepository.save(reimbursement);
        return reimbursementMapper.toDTO(updatedReimbursement);
    }

    @Override
    public void removeItem(Long reimbursementId, Long itemId) {
        Reimbursement reimbursement = reimbursementRepository.findById(reimbursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", reimbursementId));

        reimbursement.getItems().removeIf(item -> item.getId().equals(itemId));
        reimbursementRepository.save(reimbursement);
    }

    @Override
    public List<ReimbursementItemDTO> getItems(Long reimbursementId) {
        Reimbursement reimbursement = reimbursementRepository.findById(reimbursementId)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", reimbursementId));

        return reimbursement.getItems().stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ReimbursementDTO apply(Long id) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", id));
        
        // Only allow apply if status is DRAFT
        if (!ReimbursementStatus.DRAFT.equals(reimbursement.getStatus())) {
            throw new IllegalStateException("Can only apply reimbursements in DRAFT status");
        }
        
        reimbursement.setStatus(ReimbursementStatus.PENDING);
        reimbursement.setApplicationDate(LocalDate.now());
        
        return reimbursementMapper.toDTO(reimbursementRepository.save(reimbursement));
    }

    @Override
    public ReimbursementDTO approve(Long id) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", id));
        
        // Only allow approve if status is PENDING
        if (!ReimbursementStatus.PENDING.equals(reimbursement.getStatus())) {
            throw new IllegalStateException("Can only approve reimbursements in PENDING status");
        }
        
        reimbursement.setStatus(ReimbursementStatus.APPROVED);
        reimbursement.setApprovalDate(LocalDate.now());
        
        return reimbursementMapper.toDTO(reimbursementRepository.save(reimbursement));
    }

    @Override
    public ReimbursementDTO reject(Long id, String reason) {
        Reimbursement reimbursement = reimbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", id));
        
        // Only allow reject if status is PENDING
        if (!ReimbursementStatus.PENDING.equals(reimbursement.getStatus())) {
            throw new IllegalStateException("Can only reject reimbursements in PENDING status");
        }
        
        reimbursement.setStatus(ReimbursementStatus.REJECTED);
        reimbursement.setApprovalDate(LocalDate.now());
        reimbursement.setApprovalComments(reason);
        
        return reimbursementMapper.toDTO(reimbursementRepository.save(reimbursement));
    }
}
