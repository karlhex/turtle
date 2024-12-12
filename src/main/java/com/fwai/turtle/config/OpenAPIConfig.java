package com.fwai.turtle.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Turtle Enterprise Management System API",
        version = "1.0.0",
        description = "API documentation for Flyingwingsai Enterprise Management System"
    )
)
public class OpenAPIConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .group("public")
            .packagesToScan("com.fwai.turtle.controller")
            .build();
    }
}
