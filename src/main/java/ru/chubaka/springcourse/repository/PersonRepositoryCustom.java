package ru.chubaka.springcourse.repository;

import org.springframework.stereotype.Component;
import ru.chubaka.springcourse.models.Person;

import java.util.List;
import java.util.Map;

@Component
public interface PersonRepositoryCustom {
    //List<Person> showFilter(Map<String,String> filter);
    List<Person> showFilter(String name,String email);
}
