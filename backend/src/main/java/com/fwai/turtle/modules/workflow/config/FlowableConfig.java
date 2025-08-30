package com.fwai.turtle.modules.workflow.config;

import org.springframework.context.annotation.Configuration;

/**
 * Flowable工作流引擎配置
 * 
 * Flowable Spring Boot starter会自动配置ProcessEngine，
 * 我们只需要通过application.yml进行配置即可
 * 
 * @author Claude
 */
@Configuration
public class FlowableConfig {
    
    // Flowable Spring Boot starter会自动创建以下Bean:
    // - ProcessEngine
    // - RuntimeService
    // - TaskService
    // - RepositoryService
    // - HistoryService
    // - FormService
    // - IdentityService
    // - ManagementService
    
}