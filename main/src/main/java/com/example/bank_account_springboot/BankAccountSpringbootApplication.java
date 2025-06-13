package com.example.bank_account_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ro.adela.BankConfiguration;
import ro.adela.bank.BankAccountDto;

import java.util.Collection;
import java.util.HashSet;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.bank_account_springboot.controller"})
public class BankAccountSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(BankAccountSpringbootApplication.class);
		springApplication.setWebApplicationType(WebApplicationType.SERVLET);

		Collection<Class<?>> additionalPrimarySources = new HashSet<>();
		additionalPrimarySources.add(BankConfiguration.class);
		springApplication.addPrimarySources(additionalPrimarySources);
		springApplication.run(args);
	}

	// TODO Adela ComponentToScan sau @Import + @Configuration

}
