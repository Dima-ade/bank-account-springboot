package ro.adela;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import jakarta.xml.bind.JAXBException;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.service.AbstractService;
import ro.adela.bank.service.DatabaseService;
import ro.adela.bank.service.JsonFileService;
import ro.adela.bank.service.XmlFileService;
import ro.jean.AccountsConfiguration;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.*;

import static org.hibernate.cfg.AvailableSettings.*;

@Configuration
@Import(value = {AccountsConfiguration.class})
@EntityScan(basePackages = {"ro.adela.bank"})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"ro.adela.repository"})
public class BankConfiguration {

    public BankConfiguration() {
    }


//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager txManager = new JpaTransactionManager();
//        txManager.setEntityManagerFactory(entityManagerFactory);
//        return txManager;
//    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }

//    @Bean
//    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
//        return new PersistenceExceptionTranslationPostProcessor();
//    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgresPlusDialect");

        return properties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("ro.adela.bank");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());

        return em;
    }

      @Bean
    public DataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl("jdbc:postgresql://127.0.0.1:5432/accountdb");
        basicDataSource.setUsername("postgres");
        basicDataSource.setPassword("admin");
        return basicDataSource;
    }



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
}
