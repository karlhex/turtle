package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.CompanyDTO;
import com.fwai.turtle.persistence.entity.Company;
import com.fwai.turtle.persistence.repository.CompanyRepository;
import com.fwai.turtle.service.interfaces.CompanyService;
import com.fwai.turtle.persistence.mapper.CompanyMapper;
import com.fwai.turtle.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Company Service Implementation
 * 公司服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    @Override
    public Page<CompanyDTO> getCompanies(Pageable pageable, String search, Boolean active) {
        Specification<Company> spec = Specification.where(null);

        if (StringUtils.hasText(search)) {
            spec = spec.and((root, query, cb) ->
                cb.or(
                    cb.like(cb.lower(root.get("fullName")), "%" + search.toLowerCase() + "%"),
                    cb.like(cb.lower(root.get("shortName")), "%" + search.toLowerCase() + "%")
                )
            );
        }

        if (active != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("active"), active));
        }

        return companyRepository.findAll(spec, pageable)
                .map(companyMapper::toDTO);
    }

    @Override
    public List<CompanyDTO> searchCompanies(String query) {
        return companyRepository.findByFullNameContainingIgnoreCaseOrShortNameContainingIgnoreCase(query, query)
                .stream()
                .map(companyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(companyMapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
    }

    @Override
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        Company company = companyMapper.toEntity(companyDTO);
        company.setActive(true); // 新创建的公司默认为启用状态
        company = companyRepository.save(company);
        return companyMapper.toDTO(company);
    }

    @Override
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company existingCompany = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        
        companyMapper.updateEntityFromDTO(companyDTO, existingCompany);
        existingCompany = companyRepository.save(existingCompany);
        return companyMapper.toDTO(existingCompany);
    }

    @Override
    public void deleteCompany(Long id) {
        if (!companyRepository.existsById(id)) {
            throw new ResourceNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
    }

    @Override
    public CompanyDTO toggleStatus(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        
        company.setActive(!company.getActive());
        company = companyRepository.save(company);
        return companyMapper.toDTO(company);
    }
}
