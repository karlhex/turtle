package com.fwai.turtle.modules.project.repository;

import com.fwai.turtle.modules.project.entity.Contract;
import com.fwai.turtle.base.types.ContractStatus;
import com.fwai.turtle.base.types.ContractType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByContractNo(String contractNo);
    
    boolean existsByContractNo(String contractNo);

    @Query("SELECT c FROM Contract c WHERE " +
           "(:contractNo IS NULL OR c.contractNo LIKE %:contractNo%) AND " +
           "(:title IS NULL OR c.title LIKE %:title%) AND " +
           "(:company IS NULL OR c.buyerCompany.fullName LIKE %:company% OR " +
           "c.buyerCompany.shortName LIKE %:company% OR " +
           "c.sellerCompany.fullName LIKE %:company% OR " +
           "c.sellerCompany.shortName LIKE %:company%) AND " +
           "(:type IS NULL OR c.type = :type) AND " +
           "(:status IS NULL OR c.status = :status) AND " +
           "(:projectNo IS NULL OR c.projectNo LIKE %:projectNo%)")
    Page<Contract> search(
            @Param("contractNo") String contractNo,
            @Param("title") String title,
            @Param("company") String company,
            @Param("type") ContractType type,
            @Param("status") ContractStatus status,
            @Param("projectNo") String projectNo,
            Pageable pageable
    );

    List<Contract> findByStatus(ContractStatus status);

    List<Contract> findByEndDateBefore(LocalDate date);

    List<Contract> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT DISTINCT i.contract FROM Invoice i WHERE i.invoiceNo = :invoiceNo")
    List<Contract> findByInvoiceNo(@Param("invoiceNo") String invoiceNo);

    @Query("SELECT i.contract FROM Invoice i WHERE i.id = :invoiceId")
    Optional<Contract> findByInvoiceId(@Param("invoiceId") Long invoiceId);
}
