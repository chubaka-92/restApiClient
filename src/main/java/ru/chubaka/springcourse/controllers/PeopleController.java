package ru.chubaka.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.chubaka.springcourse.models.Person;
import ru.chubaka.springcourse.repository.PersonRepository;
import ru.chubaka.springcourse.validation.NoEntityException;

import java.util.List;
@EnableBinding(Source.class)
@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PersonRepository personRepository;

    @Autowired
    Source source;


    private RestTemplate restTemplate;

    @Autowired
    public PeopleController(PersonRepository personRepository,
                            RestTemplate restTemplate) {
        this.personRepository = personRepository;
        this.restTemplate = restTemplate;
    }

/*    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> search(@RequestParam("skey") String skey){
        return personRepository.findByNameContainingOrEmailContaining(skey,skey);
    }*/

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    //public List<Person> search(@RequestParam() Map<String,String> filter){
    public List<Person> search(@RequestParam(value = "name", required = false) String name,
                               @RequestParam(value = "email", required = false) String email){

        return personRepository.showFilter(name,email);
    }

    @GetMapping(value = "/goto", produces = MediaType.APPLICATION_JSON_VALUE)
    public String goTo(){
        String fooResourceUrl = "https://evilinsult.com/generate_insult.php?lang=ru&type=json";
        String response  = restTemplate.getForEntity(fooResourceUrl, String.class).getBody();
        return response;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person show(@PathVariable("id") long id){
        return  personRepository.findById(id)
                                        .orElseThrow(() -> new NoEntityException("Не найден пользователь с id: " + id));
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> showAll(){
        return (List<Person>) personRepository.findAll();
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable("id") long id) {
        personRepository.deleteById(id);
    }

    @PostMapping
    public long creat(@RequestBody Person person){
        long persId = personRepository.save(person).getId();
        source.output().send(MessageBuilder.withPayload(person).build());
        return persId;
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public long update(@RequestBody Person person){
        return personRepository.save(person).getId();
    }

}
