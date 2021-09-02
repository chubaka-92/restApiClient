package ru.chubaka.springcourse.initizer;

import org.junit.ClassRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.chubaka.springcourse.repository.PersonRepository;

/*@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = {Postgres.Initializer.class})*/
/*@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)*/
public class Postgres{
/*    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:13.1");

    @DynamicPropertySource
    public static void initializer(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
    }
*/

/*    @ClassRule
    public static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("postgres")
            .withUsername("postgres")
            .withPassword("postgres");;*//*
            .withDatabaseName("integration-tests-db")
            .withUsername("sa")
            .withPassword("sa");;*//*

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                "spring.datasource.url=" + container.getJdbcUrl(),
                "spring.datasource.username=" + container.getUsername(),
                "spring.datasource.password=" + container.getPassword()
            ).applyTo(applicationContext);
        }
    }*/
}
