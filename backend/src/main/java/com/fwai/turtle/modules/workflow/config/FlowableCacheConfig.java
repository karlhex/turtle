package com.fwai.turtle.modules.workflow.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Flowable缓存配置
 * 提高工作流查询和执行性能
 * 
 * @author Claude
 */
@Configuration
@EnableCaching
public class FlowableCacheConfig {
    
    /**
     * 开发环境使用内存缓存
     */
    @Bean
    @Profile("dev")
    public CacheManager devCacheManager() {
        return new ConcurrentMapCacheManager(
            "processDefinitions",
            "processInstances", 
            "tasks",
            "workflowStatus"
        );
    }
    
    /**
     * 生产环境使用Redis缓存
     * 需要额外配置RedisTemplate
     */
    @Bean
    @Profile("prod")
    public CacheManager prodCacheManager() {
        // 在生产环境中，这里应该配置RedisCacheManager
        // 暂时使用内存缓存作为fallback
        return new ConcurrentMapCacheManager(
            "processDefinitions",
            "processInstances", 
            "tasks",
            "workflowStatus"
        );
    }
}