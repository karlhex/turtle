package com.fwai.turtle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {
    "com.fwai.turtle.base.repository",
    "com.fwai.turtle.security.repository",
    "com.fwai.turtle.modules.**.repository"
})
@EntityScan(basePackages = {
    "com.fwai.turtle.base.entity",
    "com.fwai.turtle.security.entity",
    "com.fwai.turtle.modules.**.entity"
})
public class TurtleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TurtleApplication.class, args);
	}

}
