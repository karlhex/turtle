package com.fwai.turtle.modules.project.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.modules.project.entity.Contract;
import com.fwai.turtle.modules.project.repository.ContractRepository;

@Component
public class ContractIdMapper {
    private final ContractRepository contractRepository;

    public ContractIdMapper(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public Contract fromId(Long id) {
        if (id == null) return null;
        return contractRepository.findById(id).orElse(null);
    }

}
