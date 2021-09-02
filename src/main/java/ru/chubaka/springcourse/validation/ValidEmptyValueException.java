package ru.chubaka.springcourse.validation;

public class ValidEmptyValueException extends ValidationException {
    public ValidEmptyValueException(String description) {
        super(description);
    }

}
