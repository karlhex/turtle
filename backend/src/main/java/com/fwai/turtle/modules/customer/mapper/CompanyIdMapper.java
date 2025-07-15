package com.fwai.turtle.modules.customer.mapper;

import org.springframework.stereotype.Component;

import com.fwai.turtle.modules.customer.entity.Company;
import com.fwai.turtle.modules.customer.repository.CompanyRepository;

@Component
public class CompanyIdMapper {

    private final CompanyRepository companyRepository;

    public CompanyIdMapper(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company fromId(Long id) {
        if (id == null) return null;
        Company company = companyRepository.findById(id).orElse(null);

        return company;
    }
}
