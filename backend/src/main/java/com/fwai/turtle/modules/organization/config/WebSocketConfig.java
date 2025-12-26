package com.fwai.turtle.modules.organization.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket配置类
 * 用于配置WebSocket消息代理和端点
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用简单代理，用于向客户端发送消息
        config.enableSimpleBroker("/topic", "/queue");

        // 设置应用程序消息前缀
        config.setApplicationDestinationPrefixes("/app");

        // 设置用户特定消息前缀
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册WebSocket端点
        registry.addEndpoint("/ws/notifications")
                .setAllowedOriginPatterns("*") // 开发环境允许所有来源
                .withSockJS(); // 启用SockJS回退
    }
}