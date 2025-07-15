package com.fwai.turtle.modules.project.repository;

import com.fwai.turtle.modules.project.entity.ContractItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractItemRepository extends JpaRepository<ContractItem, Long> {
    List<ContractItem> findByContractId(Long contractId);

    @Query("SELECT ci FROM ContractItem ci WHERE " +
           "ci.contract.id = :contractId AND " +
           "(:productId IS NULL OR ci.product.id = :productId) AND " +
           "(:modelNumber IS NULL OR ci.modelNumber LIKE %:modelNumber%)")
    Page<ContractItem> search(
            @Param("contractId") Long contractId,
            @Param("productId") Long productId,
            @Param("modelNumber") String modelNumber,
            Pageable pageable
    );

    void deleteByContractId(Long contractId);
}
