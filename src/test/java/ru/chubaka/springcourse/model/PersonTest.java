package ru.chubaka.springcourse.model;

import org.junit.Before;
import org.junit.Test;
import ru.chubaka.springcourse.models.Person;
import ru.chubaka.springcourse.validation.ValidEmptyValueException;
import ru.chubaka.springcourse.validation.ValidNegativeValueException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


public class PersonTest {

    private static Person pers;

    @Before
    public void creatNewPers(){
        pers = new Person("Василий",25,"Vasy@kasy.ru");
    }

    @Test
    public void getIdTest(){
        assertEquals(0, pers.getId());
    }

    @Test
    public void getNameTest(){
        assertEquals("Василий", pers.getName());
    }

    @Test
    public void getAgeTest(){
        assertEquals(25, pers.getAge());
    }

    @Test
    public void getEmailTest(){
        assertEquals("Vasy@kasy.ru", pers.getEmail());
    }

    @Test
    public void setIdTest(){
        pers.setId(100);
        assertEquals(100, pers.getId());
    }

    @Test
    public void setNameTest(){
        pers.setName("Гриша");
        assertEquals("Гриша", pers.getName());
    }

    @Test
    public void setAgeTest(){
        pers.setAge(45);
        assertEquals(45, pers.getAge());
    }

    @Test
    public void setEmailTest(){
        pers.setEmail("heppy@mail.ru");
        assertEquals("heppy@mail.ru", pers.getEmail());
    }

    /**
     * Негативные кейсы
     */
    @Test
    public void setEmptyNameExeptionTest(){
        ValidEmptyValueException valid = assertThrows(ValidEmptyValueException.class, () -> { pers.setName(""); });
        assertEquals("Имя не может быть пустым", valid.getMessage());
    }

    @Test
    public void setEmailNotValidOneExeptionTest(){
        ValidNegativeValueException valid = assertThrows(ValidNegativeValueException.class, () -> { pers.setEmail("blabla.bla"); });
        assertEquals("Не корректный Email", valid.getMessage());
    }

    @Test
    public void setEmailNotValidTwoExeptionTest(){
        ValidNegativeValueException valid = assertThrows(ValidNegativeValueException.class, () -> { pers.setEmail("blab@labla"); });
        assertEquals("Не корректный Email", valid.getMessage());
    }

    @Test
    public void setNegativeAgeExeptionTest(){
        ValidNegativeValueException valid = assertThrows(ValidNegativeValueException.class, () -> { pers.setAge(-4); });
        assertEquals("Возраст не может быть отрицательным", valid.getMessage());
    }
}
