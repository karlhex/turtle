package com.fwai.turtle.modules.project.service.impl;

import com.fwai.turtle.modules.project.dto.ContractDownPaymentDTO;
import com.fwai.turtle.base.exception.ResourceNotFoundException;
import com.fwai.turtle.modules.project.entity.Contract;
import com.fwai.turtle.modules.project.entity.ContractDownPayment;
import com.fwai.turtle.modules.finance.entity.Currency;
import com.fwai.turtle.modules.project.mapper.ContractDownPaymentMapper;
import com.fwai.turtle.modules.project.repository.ContractDownPaymentRepository;
import com.fwai.turtle.modules.project.repository.ContractRepository;
import com.fwai.turtle.modules.finance.repository.CurrencyRepository;
import com.fwai.turtle.modules.project.service.ContractDownPaymentService;
import com.fwai.turtle.base.types.DebitCreditType;

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
public class ContractDownPaymentServiceImpl implements ContractDownPaymentService {
    private final ContractDownPaymentRepository paymentRepository;
    private final ContractRepository contractRepository;
    private final CurrencyRepository currencyRepository;
    private final ContractDownPaymentMapper paymentMapper;

    @Override
    public ContractDownPaymentDTO create(ContractDownPaymentDTO paymentDTO) {
        ContractDownPayment payment = paymentMapper.toEntity(paymentDTO);
        
        // 设置合同
        Contract contract = contractRepository.findById(paymentDTO.getContractId())
                .orElseThrow(() -> new ResourceNotFoundException("Contract", "id", paymentDTO.getContractId()));
        payment.setContract(contract);
        
        // 设置币种
        Currency currency = currencyRepository.findById(paymentDTO.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", paymentDTO.getCurrencyId()));
        payment.setCurrency(currency);

        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public ContractDownPaymentDTO update(Long id, ContractDownPaymentDTO paymentDTO) {
        ContractDownPayment existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractDownPayment", "id", id));

        ContractDownPayment payment = paymentMapper.toEntity(paymentDTO);
        payment.setId(id);
        payment.setContract(existingPayment.getContract());  // 保持原有合同关联

        // 设置币种
        Currency currency = currencyRepository.findById(paymentDTO.getCurrencyId())
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", paymentDTO.getCurrencyId()));
        payment.setCurrency(currency);

        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new ResourceNotFoundException("ContractDownPayment", "id", id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ContractDownPaymentDTO getById(Long id) {
        ContractDownPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractDownPayment", "id", id));
        return paymentMapper.toDTO(payment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDownPaymentDTO> getByContractId(Long contractId) {
        return paymentRepository.findByContractId(contractId).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContractDownPaymentDTO> search(
            Long contractId,
            DebitCreditType debitCreditType,
            LocalDate startDate,
            LocalDate endDate,
            Boolean paymentStatus,
            Pageable pageable
    ) {
        return paymentRepository.search(contractId, debitCreditType, startDate, endDate, paymentStatus, pageable)
                .map(paymentMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContractDownPaymentDTO> getOverduePayments(LocalDate date) {
        return paymentRepository.findByPaymentStatusFalseAndPlannedDateBefore(date).stream()
                .map(paymentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ContractDownPaymentDTO confirmPayment(Long id, LocalDate actualDate) {
        ContractDownPayment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContractDownPayment", "id", id));
        
        payment.setActualDate(actualDate);
        payment.setPaymentStatus(true);
        
        payment = paymentRepository.save(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public void deleteByContractId(Long contractId) {
        paymentRepository.deleteByContractId(contractId);
    }
}
