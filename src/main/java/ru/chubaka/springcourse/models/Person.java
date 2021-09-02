package ru.chubaka.springcourse.models;

import ru.chubaka.springcourse.validation.ValidEmptyValueException;
import ru.chubaka.springcourse.validation.ValidNegativeValueException;

import javax.persistence.*;

/*@Data
@AllArgsConstructor
@Builder*/
@Entity
@Table(name="person")
public class Person {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String name;

    private int age;

    private String email;

    public Person() {}

    public Person(String name, int age ,String email){
        setName(name);
        setAge(age);
        this.email = email;
    }

    public void setName(String name) {
        if(!name.isEmpty()){
            this.name = name;
        } else {
            throw new ValidEmptyValueException("Имя не может быть пустым");
        }
            this.name = name;
        }

    public long getId() {
            return id;
       }

    public String getName() {
            return name;
        }

    public int getAge() {
            return age;
        }

    public String getEmail() {
            return email;
        }

    public void setAge(int age) {
        if(age < 0){
            throw new ValidNegativeValueException("Возраст не может быть отрицательным");
        } else {
            this.age = age;
        }
    }

    public void setId(long id) {
            this.id = id;
        }

    public void setEmail(String email) {
        if(!email.isEmpty()) {
            if (email.contains("@") && email.contains(".")) {
                this.email = email;
            } else {
                throw new ValidNegativeValueException("Не корректный Email");
            }
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
