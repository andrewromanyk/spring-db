package ua.edu.ukma.springdb;


import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
@EnableTransactionManagement
@EnableMongoRepositories(basePackages = "ua.edu.ukma.springdb")
public class Config {

}

