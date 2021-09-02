package ru.chubaka.springcourse.validation;

public class NoEntityException extends RuntimeException{
    public NoEntityException(String description){
        super(description);
    }
}
