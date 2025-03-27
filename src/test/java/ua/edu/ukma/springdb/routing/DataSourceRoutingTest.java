package ua.edu.ukma.springdb.routing;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.edu.ukma.springdb.entity.Author;
import ua.edu.ukma.springdb.service.AuthorService;

import java.time.LocalDate;

@Testcontainers
@SpringBootTest
public class DataSourceRoutingTest {

    @Autowired
    private DataSourceContext dataSourceContext;

    @Autowired
    private AuthorService authorService;

    @Container
    public static PostgreSQLContainer<?> primaryDb = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("mydb")
            .withUsername("user")
            .withPassword("a")
            .withNetworkAliases("primary-db")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(5434), new ExposedPort(5432))
                    )
            ));

    @Container
    public static PostgreSQLContainer<?> secondaryDb = new PostgreSQLContainer<>("postgres:14")
            .withDatabaseName("mydb")
            .withUsername("user")
            .withPassword("a")
            .withNetworkAliases("secondary-db")
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(
                            new PortBinding(Ports.Binding.bindPort(5435), new ExposedPort(5432))
                    )
            ));

    @Test
    public void testDataSourceRouting() {
        System.out.println(primaryDb.getJdbcUrl());
        System.out.println(secondaryDb.getJdbcUrl());

        dataSourceContext.setBranchContext("main");
        Author auth1 = new Author("AC/DC", "Group AC/DC", LocalDate.of(1973, 11, 1));
        authorService.createAuthor(auth1);

        dataSourceContext.setBranchContext("sec");
        Author auth2 = new Author("AC/DC", "Group AC/DC 2", LocalDate.of(1973, 11, 1));
        authorService.createAuthor(auth2);

        dataSourceContext.setBranchContext("main");
        Assertions.assertEquals("Group AC/DC", authorService.getAuthorByName("AC/DC").get().getDescription());

        dataSourceContext.setBranchContext("main");
        Assertions.assertEquals("Group AC/DC 2", authorService.getAuthorByName("AC/DC").get().getDescription());
    }

}
