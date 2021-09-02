package ru.chubaka.springcourse.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.chubaka.springcourse.models.Person;

import java.util.List;
@Repository
public interface PersonRepository extends CrudRepository<Person, Long>, PersonRepositoryCustom {
    List<Person> findByNameContaining(String partName);
    Person findByName(String partName);
    List<Person> findByNameContainingOrEmailContaining(String partName, String partEmail);

}

