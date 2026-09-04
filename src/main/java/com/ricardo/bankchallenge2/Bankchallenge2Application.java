package com.ricardo.bankchallenge2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Bank Challenge 2 API", version = "1.0",description = "RESTful banking operations API built with Spring Boot"))
@SpringBootApplication
public class Bankchallenge2Application {

	public static void main(String[] args) {
		SpringApplication.run(Bankchallenge2Application.class, args);
	}

}
