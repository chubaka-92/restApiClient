package ru.chubaka.springcourse;

import kafka.server.KafkaConfig;
import org.apache.zookeeper.ZooKeeper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.AbstractMessageChannel;
import org.springframework.integration.config.GlobalChannelInterceptor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.chubaka.springcourse.initizer.Postgres;
import ru.chubaka.springcourse.kafka.Consumer;
import ru.chubaka.springcourse.models.Person;
import ru.chubaka.springcourse.repository.PersonRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import static org.assertj.core.api.Assertions.*;
//import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringRestAppTest<assertionInterceptor> {
    @Container
    public static PostgreSQLContainer container = new PostgreSQLContainer("postgres:13.1");

    @Autowired
    Source source;

    @Autowired
    private MessageCollector collector;


    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @AfterEach
    public void resetDb() {
        personRepository.deleteAll();
    }

    @DynamicPropertySource
    public static void initializer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
    }


    private Person createTestPerson(String name, int age, String email) {
        Person person = new Person(name,age,email);
        return personRepository.save(person);
    }

    @Test
    public void ctreatPositiveTest() throws InterruptedException {
        Person person = new Person("Symon",65,"trueDedushka@mai.tr");

        ResponseEntity<Long> response = restTemplate.postForEntity("/people",person,Long.class);
        Person personJdbc = jdbcTemplate.query("SELECT * FROM Person WHERE id="+response.getBody(),
                new BeanPropertyRowMapper<>(Person.class)).get(0);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(personJdbc.getName(), person.getName());
        assertEquals(personJdbc.getAge(), person.getAge());
        assertEquals(personJdbc.getEmail(), person.getEmail());

        //BlockingQueue<Message<?>> messages = collector.forChannel(source.output());
        //String message2 = (String) messages.take().getPayload();
        //assertThat(messages).isNotNull();

        Message<?> message3 = collector.forChannel(source.output()).poll();
        System.out.println("Message 3: "+ message3.getPayload());
        assertThat(message3).isNotNull();
        assertThat(message3.getPayload()).isEqualTo("{\"id\":"+personJdbc.getId()+"," +
                                                    "\"name\":\""+personJdbc.getName()+"\"," +
                                                    "\"age\":"+personJdbc.getAge()+"," +
                                                    "\"email\":\""+personJdbc.getEmail()+"\"}");
    }

    @Test
    //@Sql("/sql/data.sql")
    public void showPositiveTest(){
        long id = createTestPerson("Symon",65,"trueDedushka@mai.tr").getId();

        Person personResp = restTemplate.getForObject("/people/{id}",Person.class, id);
        Person personJdbc = jdbcTemplate.query("SELECT * FROM Person WHERE id="+id,new BeanPropertyRowMapper<>(Person.class)).get(0);

        assertEquals(personResp.getName(), personJdbc.getName());
        assertEquals(personResp.getAge(), personJdbc.getAge());
        assertEquals(personResp.getEmail(), personJdbc.getEmail());
    }

    @Test
    public void searchPositiveTest(){
        createTestPerson("Symon",65,"trueDedushka@mai.tr");

        ResponseEntity<List<Person>> response =restTemplate.exchange("/people?name=Symon&email=trueDedushka@mai.tr",
                                    HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {});

        List<Person> personsResp = response.getBody();

        Person personJdbc = jdbcTemplate.query("SELECT * FROM Person WHERE name='Symon' AND email='trueDedushka@mai.tr'",
                                                new BeanPropertyRowMapper<>(Person.class)).get(0);

        assertEquals(personsResp.size(), 1);
        assertEquals(personsResp.get(0).getName(), personJdbc.getName());
        assertEquals(personsResp.get(0).getAge(), personJdbc.getAge());
        assertEquals(personsResp.get(0).getEmail(), personJdbc.getEmail());

    }

    @Test
    public void deletePositiveTest(){
        Person person = createTestPerson("Symon",65,"trueDedushka@mai.tr");
        long id = person.getId();

        ResponseEntity<Person> response = restTemplate.exchange("/people/{id}", HttpMethod.DELETE, null, Person.class, id);

        List<Person> personJdbc = jdbcTemplate.query("SELECT * FROM Person WHERE id="+id,new BeanPropertyRowMapper<>(Person.class));

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(personRepository.count(), personJdbc.size());
    }


    @Test
    public void updatePositiveTest(){
        long id = createTestPerson("Symon",65,"trueDedushka@mai.tr").getId();

        Person updatePerson = new Person("Mike",26,"mimimi@mai.tr");
        updatePerson.setId(id);

        HttpEntity<Person> entity = new HttpEntity<>(updatePerson);

        ResponseEntity<Long> response =restTemplate.exchange("/people", HttpMethod.PUT, entity, Long.class, id);

        Person personJdbc = jdbcTemplate.query("SELECT * FROM Person WHERE id="+id,new BeanPropertyRowMapper<>(Person.class)).get(0);


        assertEquals(response.getStatusCode(), HttpStatus.OK);

        assertEquals(personJdbc.getName(), updatePerson.getName());
        assertEquals(personJdbc.getAge(), updatePerson.getAge());
        assertEquals(personJdbc.getEmail(), updatePerson.getEmail());

    }

    @Test
    public void showAllPositiveTest(){
        createTestPerson("Symon",65,"trueDedushka@mai.tr");
        createTestPerson("Mumu",35,"Mumu@mai.tr");
        createTestPerson("Nafnaf",45,"Nafnaf@mai.tr");

        ResponseEntity<List<Person>> response =restTemplate.exchange("/people/all",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<Person>>() {});

        List<Person> persons = response.getBody();

        List<Person> personJdbc = jdbcTemplate.query("SELECT * FROM Person",new BeanPropertyRowMapper<>(Person.class));

        assertEquals(persons.size(), personJdbc.size());
        assertEquals(persons.get(0).getName(), personJdbc.get(0).getName());
        assertEquals(persons.get(0).getAge(), personJdbc.get(0).getAge());
        assertEquals(persons.get(0).getEmail(), personJdbc.get(0).getEmail());

    }
}
