package ua.edu.ukma.springdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
public class SpringDbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDbApplication.class, args);
    }

}
