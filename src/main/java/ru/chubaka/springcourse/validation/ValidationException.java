package ru.chubaka.springcourse.validation;

public class ValidationException extends RuntimeException{
    public ValidationException(String description){
        super(description);
    }


}
