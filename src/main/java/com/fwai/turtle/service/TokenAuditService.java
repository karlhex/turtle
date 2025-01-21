package com.fwai.turtle.service;

import com.fwai.turtle.persistence.entity.TokenAuditLog;
import com.fwai.turtle.persistence.repository.TokenAuditRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class TokenAuditService {
    private final TokenAuditRepository auditRepository;
    
    public TokenAuditService(TokenAuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }
    
    public void logTokenEvent(String username, String tokenId, String eventType) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            // Handle the case when we're not in a web request context
            TokenAuditLog log = TokenAuditLog.builder()
                .username(username)
                .tokenId(tokenId)
                .eventType(eventType)
                .timestamp(LocalDateTime.now())
                .ipAddress("N/A")
                .userAgent("N/A")
                .build();
                
            auditRepository.save(log);
            return;
        }
        
        HttpServletRequest request = attributes.getRequest();
        TokenAuditLog log = TokenAuditLog.builder()
            .username(username)
            .tokenId(tokenId)
            .eventType(eventType)
            .timestamp(LocalDateTime.now())
            .ipAddress(request.getRemoteAddr())
            .userAgent(request.getHeader("User-Agent"))
            .build();
            
        auditRepository.save(log);
    }
}
