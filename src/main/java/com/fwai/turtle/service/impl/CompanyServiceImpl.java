package com.fwai.turtle.service.impl;

import com.fwai.turtle.dto.CompanyDTO;
import com.fwai.turtle.dto.BankAccountDTO;
import com.fwai.turtle.dto.TaxInfoDTO;
import com.fwai.turtle.persistence.entity.Company;
import com.fwai.turtle.persistence.entity.BankAccount;
import com.fwai.turtle.persistence.repository.CompanyRepository;
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
import com.fwai.turtle.types.CompanyType;

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


    @Override
    public Page<CompanyDTO> getCompanies(Pageable pageable, String search, Boolean active) {
        Specification<Company> spec = (root, query, cb) -> {

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
        
        // if type == PRIMARY, check if another PRIMARY company exists
        if (company.getType() == CompanyType.PRIMARY) {
            if (companyRepository.findByTypeAndActiveTrue(CompanyType.PRIMARY).isPresent()) {
                throw new IllegalArgumentException("Only one PRIMARY company is allowed");
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

        // if type == PRIMARY, check if another PRIMARY company exists

        if (companyDTO.getType() == CompanyType.PRIMARY) {
            Company primaryCompany = companyRepository.findByTypeAndActiveTrue(CompanyType.PRIMARY).orElse(null);
            if (primaryCompany != null && !primaryCompany.getId().equals(id)) {
                throw new IllegalArgumentException("Only one PRIMARY company is allowed");
            }
        }

        companyMapper.updateEntityFromDTO(companyDTO, company);
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

    @Override
    public CompanyDTO findCompanyByType(CompanyType type) {
        Company company = companyRepository.findByTypeAndActiveTrue(type)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with type: " + type));

        return companyMapper.toDTO(company);
    }

    @Override
    @Transactional
    public CompanyDTO setPrimary(Long id) {
        // Find the company to be set as primary
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + id));

        // Check if company is active
        if (!company.getActive()) {
            throw new IllegalArgumentException("Cannot set inactive company as primary");
        }

        // Find current primary company if exists
        companyRepository.findByTypeAndActiveTrue(CompanyType.PRIMARY)
                .ifPresent(primaryCompany -> {
                    primaryCompany.setType(CompanyType.CUSTOMER);
                    companyRepository.save(primaryCompany);
                });

        // Set the new company as primary
        company.setType(CompanyType.PRIMARY);
        return companyMapper.toDTO(companyRepository.save(company));
    }
}
