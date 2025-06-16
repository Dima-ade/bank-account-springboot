package ro.adela;

import jakarta.persistence.EntityManagerFactory;
import jakarta.xml.bind.JAXBException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import ro.adela.bank.service.AbstractService;
import ro.adela.bank.service.DatabaseService;
import ro.adela.bank.service.JsonFileService;
import ro.adela.bank.service.XmlFileService;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.bank_account_springboot.controller"})
public class BankAccountSpringbootApplication {

	@Bean
	public AbstractService createService(Environment environment, EntityManagerFactory emf)
			throws JAXBException {

		String fileType = environment.getProperty("service.type");
		AbstractService service;
		if (fileType.equals("xml")) {
			File file = new File("account.xml");
			service = new XmlFileService(file);
		} else if (fileType.equals("json")) {
			File file = new File("account.json");
			service = new JsonFileService(file);
		} else if (fileType.equals("db")) {
			service = new DatabaseService(emf);
		} else {
			throw new IllegalArgumentException(String.format("Unknown type %s.", fileType));
		}
		return service;
	}

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(BankAccountSpringbootApplication.class);
		springApplication.setWebApplicationType(WebApplicationType.SERVLET);

		Collection<Class<?>> additionalPrimarySources = new HashSet<Class<?>>();
		additionalPrimarySources.add(DatabaseConfiguration.class);
		springApplication.addPrimarySources(additionalPrimarySources);
		springApplication.run(args);
	}

	// TODO Adela ComponentToScan sau @Import + @Configuration

}
