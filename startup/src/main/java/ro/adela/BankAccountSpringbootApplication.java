package ro.adela;

import jakarta.persistence.EntityManagerFactory;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ro.adela.bank.service.*;
import ro.adela.controller.Controller;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

@SpringBootApplication
@ComponentScan(basePackages = {"ro.adela"})
@Import({Controller.class, ApplicationExceptionsHandler.class})
public class BankAccountSpringbootApplication {

	@Bean
	public AbstractService createService(Environment environment, PersistenceManager persistenceManager)//, EntityManagerFactory emf)
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
			service = new JpaDatabaseService(persistenceManager);
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
