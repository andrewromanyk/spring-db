package ua.edu.ukma.springdb;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ua.edu.ukma.springdb.routing.DataSourceContext;
import ua.edu.ukma.springdb.routing.DataSourceRoutingImpl;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@EnableTransactionManagement
public class Config {

    @Bean(name = "mainDb")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource1 = new DriverManagerDataSource();
        dataSource1.setDriverClassName("org.postgresql.Driver");
        dataSource1.setUrl("jdbc:postgresql://localhost:5434/mydb");
        dataSource1.setUsername("user");
        dataSource1.setPassword("a");
        return dataSource1;
    }

    @Bean(name = "sndDb")
    public DataSource dataSource2() {
        DriverManagerDataSource dataSource2 = new DriverManagerDataSource();
        dataSource2.setDriverClassName("org.postgresql.Driver");
        dataSource2.setUrl("jdbc:postgresql://localhost:5435/mydb");
        dataSource2.setUsername("user");
        dataSource2.setPassword("a");
        return dataSource2;
    }

}

