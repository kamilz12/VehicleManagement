package com.kamilz12.vehiclemanagementsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = "com.kamilz12.vehiclemanagementsystem.model")
@EnableJpaRepositories(basePackages = "com.kamilz12.vehiclemanagementsystem.repository")
@SpringBootApplication
public class VehicleManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleManagementSystemApplication.class, args);
	}

}
