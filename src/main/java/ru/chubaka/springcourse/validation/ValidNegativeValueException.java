package ru.chubaka.springcourse.validation;

public class ValidNegativeValueException extends ValidationException {
    public ValidNegativeValueException(String description) {
        super(description);
    }
}
