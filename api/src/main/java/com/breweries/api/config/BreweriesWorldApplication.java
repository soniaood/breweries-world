package com.breweries.api.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;

@SpringBootApplication
@ComponentScan(basePackages = "com.breweries")
@EnableGlobalAuthentication
public class BreweriesWorldApplication {

	public static void main(String[] args) {
		SpringApplication.run(BreweriesWorldApplication.class, args);
	}

}
