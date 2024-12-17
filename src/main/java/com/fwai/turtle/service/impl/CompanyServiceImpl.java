package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.CompanyDTO;
import com.fwai.turtle.dto.PersonDTO;
import com.fwai.turtle.dto.TaxInfoDTO;
import com.fwai.turtle.dto.BankAccountDTO;

import com.fwai.turtle.persistence.entity.Company;
import com.fwai.turtle.persistence.entity.BankAccount;
import com.fwai.turtle.persistence.repository.CompanyRepository;
import com.fwai.turtle.persistence.repository.PersonRepository;
import com.fwai.turtle.persistence.repository.TaxInfoRepository;
import com.fwai.turtle.persistence.mapper.CompanyMapper;
import com.fwai.turtle.persistence.mapper.BankAccountMapper;
import com.fwai.turtle.service.interfaces.CompanyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.fwai.turtle.exception.ResourceNotFoundException;
import com.fwai.turtle.persistence.entity.TaxInfo;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Company Service Implementation
 * 公司服务实现类
 */
@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final TaxInfoRepository taxInfoRepository;
    private final BankAccountMapper bankAccountMapper;
    private final PersonRepository personRepository;

    @Override
    public Page<CompanyDTO> getCompanies(Pageable pageable, String search, Boolean active) {
        Specification<Company> spec = (root, query, cb) -> {
            // Add default sorting by isPrimary
            query.orderBy(
                cb.desc(root.get("isPrimary")),
                cb.asc(root.get("fullName"))
            );

            if (search != null && !search.trim().isEmpty()) {
                String searchPattern = "%" + search.toLowerCase() + "%";
                return cb.or(
                    cb.like(cb.lower(root.get("fullName")), searchPattern),
                    cb.like(cb.lower(root.get("shortName")), searchPattern),
                    cb.like(cb.lower(root.get("address")), searchPattern),
                    cb.like(cb.lower(root.get("phone")), searchPattern),
                    cb.like(cb.lower(root.get("email")), searchPattern)
                );
            }
            if (active != null) {
                return cb.equal(root.get("active"), active);
            }
            return null;
        };

        return companyRepository.findAll(spec, pageable).map(companyMapper::toDTO);
    }

    @Override
    public List<CompanyDTO> searchCompanies(String query) {
        return companyRepository.searchCompanies(query).stream()
                .map(companyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        return companyRepository.findById(id)
                .map(companyMapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
    }

    @Override
    @Transactional
    public CompanyDTO createCompany(CompanyDTO companyDTO) {
        if (companyRepository.existsByFullNameIgnoreCase(companyDTO.getFullName())) {
            throw new IllegalArgumentException("Company with this name already exists");
        }
        Company company = companyMapper.toEntity(companyDTO);
        
        // Handle TaxInfo relationship
        if (companyDTO.getTaxInfo() != null) {
            if (companyDTO.getTaxInfo().getId() != null) {
                // If TaxInfo has ID, use existing one
                company.setTaxInfo(taxInfoRepository.findById(companyDTO.getTaxInfo().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("TaxInfo", "id", companyDTO.getTaxInfo().getId())));
            } else {
                // Check if TaxInfo with same bank account exists
                String bankAccount = companyDTO.getTaxInfo().getBankAccount();
                if (bankAccount != null) {
                    taxInfoRepository.findByBankAccount(bankAccount).ifPresent(existingTaxInfo -> {
                        throw new IllegalArgumentException("TaxInfo with bank account " + bankAccount + " already exists");
                    });
                }
            }
        }
        
        company.setActive(true);
        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    @Transactional
    public CompanyDTO updateCompany(Long id, CompanyDTO companyDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        
        if (!company.getFullName().equalsIgnoreCase(companyDTO.getFullName()) &&
            companyRepository.existsByFullNameIgnoreCase(companyDTO.getFullName())) {
            throw new IllegalArgumentException("Company with this name already exists");
        }

        // Handle TaxInfo relationship before mapping
        if (companyDTO.getTaxInfo() != null) {
            if (companyDTO.getTaxInfo().getId() != null) {
                // If TaxInfo has ID, use existing one
                TaxInfoDTO taxInfoDTO = companyDTO.getTaxInfo();
                TaxInfo existingTaxInfo = taxInfoRepository.findById(taxInfoDTO.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("TaxInfo", "id", taxInfoDTO.getId()));
                
                // Check if bank account is being changed and if it conflicts with another record
                if (!existingTaxInfo.getBankAccount().equals(taxInfoDTO.getBankAccount())) {
                    taxInfoRepository.findByBankAccount(taxInfoDTO.getBankAccount())
                            .filter(other -> !other.getId().equals(existingTaxInfo.getId()))
                            .ifPresent(conflict -> {
                                throw new IllegalArgumentException("TaxInfo with bank account " + taxInfoDTO.getBankAccount() + " already exists");
                            });
                }
                company.setTaxInfo(existingTaxInfo);
            } else {
                // Check if new TaxInfo's bank account exists
                String bankAccount = companyDTO.getTaxInfo().getBankAccount();
                if (bankAccount != null) {
                    taxInfoRepository.findByBankAccount(bankAccount).ifPresent(existingTaxInfo -> {
                        throw new IllegalArgumentException("TaxInfo with bank account " + bankAccount + " already exists");
                    });
                }
            }
        }

        companyMapper.updateEntityFromDTO(companyDTO, company);

        // 更新关联的联系人信息
        PersonDTO businessContact = companyDTO.getBusinessContact();
        if (businessContact != null) {
            company.setBusinessContact(personRepository.findById(businessContact.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Person", "id", businessContact.getId())));
        }
        PersonDTO technicalContact = companyDTO.getTechnicalContact();
        if (technicalContact != null) {
            company.setTechnicalContact(personRepository.findById(technicalContact.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Person", "id", technicalContact.getId())));
        }
        
        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    @Transactional
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public List<CompanyDTO> getAllActive() {
        List<Company> activeCompanies = companyRepository.findByActive(true);
        return activeCompanies.stream()
                .map(companyMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyDTO toggleCompanyStatus(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        company.setActive(!company.getActive());
        return companyMapper.toDTO(companyRepository.save(company));
    }

    @Override
    public CompanyDTO getPrimaryCompany() {
        return companyRepository.findByIsPrimaryTrue()
                .map(companyMapper::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public CompanyDTO setPrimaryCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        
        companyRepository.resetAllPrimaryFlags();
        companyRepository.setPrimaryCompany(id);
        
        company.setIsPrimary(true);
        return companyMapper.toDTO(company);
    }

    @Override
    public List<BankAccountDTO> getCompanyBankAccounts(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        return company.getBankAccounts().stream()
                .map(bankAccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyDTO addBankAccount(Long id, BankAccountDTO bankAccountDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));
        
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTO);
        bankAccount.setCompanyId(company.getId());
        company.getBankAccounts().add(bankAccount);
        
        companyRepository.save(company);
        
        return companyMapper.toDTO(company);
    }

    @Override
    @Transactional
    public void removeBankAccount(Long companyId, Long bankAccountId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + companyId));
        
        company.getBankAccounts().removeIf(account -> account.getId().equals(bankAccountId));
        companyRepository.save(company);
    }
}
