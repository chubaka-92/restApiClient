package ru.chubaka.springcourse.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.chubaka.springcourse.models.Person;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonRepositoryTest  {

    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:13.1");

    @Autowired
    private PersonRepository personRepository;

    @DynamicPropertySource
    public static void initialize(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
    }

    @Test
    public void contexLoads(){
        System.out.println("I can!");
    }

    @Test
    @Sql("/sql/data.sql")
    public void testFindByName(){
        Person person = personRepository.findByName("Interga");
        Assertions.assertEquals("Interga", person.getName());
    }
}
