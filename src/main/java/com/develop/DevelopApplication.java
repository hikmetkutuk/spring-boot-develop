package com.develop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication()
public class DevelopApplication
{
	public static void main(String[] args) {
		SpringApplication.run(DevelopApplication.class, args);
	}
}
