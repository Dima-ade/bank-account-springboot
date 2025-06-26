package ro.adela;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.adela.bank.service.PersistenceManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@Import({PersistenceManager.class})
@EntityScan(basePackages = {"ro.adela.bank"})
@EnableJpaRepositories(basePackages ="ro.adela.bank.jpa.repository")

public class DatabaseConfiguration {

    public DatabaseConfiguration() {
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
}
