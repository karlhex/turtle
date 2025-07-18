package com.fwai.turtle.modules.finance.service.impl;

import com.fwai.turtle.modules.finance.dto.ReimbursementItemDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.finance.entity.Reimbursement;
import com.fwai.turtle.modules.finance.entity.ReimbursementItem;
import com.fwai.turtle.modules.finance.mapper.ReimbursementItemMapper;
import com.fwai.turtle.modules.finance.repository.ReimbursementItemRepository;
import com.fwai.turtle.modules.finance.repository.ReimbursementRepository;
import com.fwai.turtle.modules.finance.service.ReimbursementItemService;
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
public class ReimbursementItemServiceImpl implements ReimbursementItemService {
    private final ReimbursementItemRepository itemRepository;
    private final ReimbursementRepository reimbursementRepository;
    private final ReimbursementItemMapper itemMapper;

    @Override
    public Page<ReimbursementItemDTO> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable)
                .map(itemMapper::toDTO);
    }

    @Override
    public ReimbursementItemDTO getById(Long id) {
        return itemRepository.findById(id)
                .map(itemMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("ReimbursementItem", "id", id));
    }

    @Override
    public ReimbursementItemDTO create(ReimbursementItemDTO itemDTO) {
        // 验证报销单是否存在
        Reimbursement reimbursement = reimbursementRepository.findById(itemDTO.reimbursementId())
                .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", itemDTO.reimbursementId()));

        ReimbursementItem item = itemMapper.toEntity(itemDTO);
        item.setReimbursement(reimbursement);

        item = itemRepository.save(item);
        return itemMapper.toDTO(item);
    }

    @Override
    public ReimbursementItemDTO update(Long id, ReimbursementItemDTO itemDTO) {
        ReimbursementItem existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReimbursementItem", "id", id));

        // 如果报销单ID有变化，验证新的报销单是否存在
        if (!existingItem.getReimbursement().getId().equals(itemDTO.reimbursementId())) {
            Reimbursement newReimbursement = reimbursementRepository.findById(itemDTO.reimbursementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reimbursement", "id", itemDTO.reimbursementId()));
            existingItem.setReimbursement(newReimbursement);
        }

        itemMapper.updateEntityFromDTO(itemDTO, existingItem);
        existingItem = itemRepository.save(existingItem);
        return itemMapper.toDTO(existingItem);
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ResourceNotFoundException("ReimbursementItem", "id", id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public List<ReimbursementItemDTO> getByReimbursement(Long reimbursementId) {
        return itemRepository.findByReimbursementId(reimbursementId).stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReimbursementItemDTO> getByDateRange(LocalDate startDate, LocalDate endDate) {
        return itemRepository.findByOccurrenceDateBetween(startDate, endDate, Pageable.unpaged()).stream()
                .map(itemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ReimbursementItemDTO> searchByReason(String reason, Pageable pageable) {
        return itemRepository.findByReasonContaining(reason, pageable)
                .map(itemMapper::toDTO);
    }
}
