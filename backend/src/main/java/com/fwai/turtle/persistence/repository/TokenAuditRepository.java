package com.fwai.turtle.persistence.repository;

import com.fwai.turtle.persistence.entity.TokenAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenAuditRepository extends JpaRepository<TokenAuditLog, Long> {
}
