package com.ai_tutor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "com.ai_tutor")
public class AiInvestmentTutorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiInvestmentTutorApplication.class, args);
	}

}
