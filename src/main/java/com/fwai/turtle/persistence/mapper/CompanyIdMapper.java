package com.fwai.turtle.persistence.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.persistence.entity.Company;
import com.fwai.turtle.persistence.repository.CompanyRepository;

@Component
public class CompanyIdMapper {

    private final CompanyRepository companyRepository;

    public CompanyIdMapper(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company fromId(Long id) {
        if (id == null) return null;
        return companyRepository.findById(id).orElse(null);
    }
}
