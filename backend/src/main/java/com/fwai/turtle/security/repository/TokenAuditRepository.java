package com.fwai.turtle.security.repository;

import com.fwai.turtle.security.entity.TokenAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenAuditRepository extends JpaRepository<TokenAuditLog, Long> {
}
