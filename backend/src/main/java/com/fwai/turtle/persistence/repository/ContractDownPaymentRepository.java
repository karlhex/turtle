package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.ContractDownPayment;
import com.fwai.turtle.types.DebitCreditType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ContractDownPaymentRepository extends JpaRepository<ContractDownPayment, Long> {
    List<ContractDownPayment> findByContractId(Long contractId);

    @Query("SELECT cdp FROM ContractDownPayment cdp WHERE " +
           "cdp.contract.id = :contractId AND " +
           "(:debitCreditType IS NULL OR cdp.debitCreditType = :debitCreditType) AND " +
           "(:startDate IS NULL OR cdp.plannedDate >= :startDate) AND " +
           "(:endDate IS NULL OR cdp.plannedDate <= :endDate) AND " +
           "(:paymentStatus IS NULL OR cdp.paymentStatus = :paymentStatus)")
    Page<ContractDownPayment> search(
            @Param("contractId") Long contractId,
            @Param("debitCreditType") DebitCreditType debitCreditType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("paymentStatus") Boolean paymentStatus,
            Pageable pageable
    );

    List<ContractDownPayment> findByPaymentStatusFalseAndPlannedDateBefore(LocalDate date);

    void deleteByContractId(Long contractId);
}
