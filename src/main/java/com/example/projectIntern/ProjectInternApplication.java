package com.example.projectIntern;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectInternApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectInternApplication.class, args);
	}

}
