package ru.chubaka.springcourse.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.chubaka.springcourse.validation.*;

import java.util.Date;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(ValidEmptyValueException.class)
    public ResponseEntity<?> handleValidEmptyValueException(ValidEmptyValueException exception, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        exception.getMessage(),
                                                        request.getDescription(false));

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidNegativeValueException.class)
    public ResponseEntity<?> handleValidNegativeValueException(ValidNegativeValueException exception, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        exception.getMessage(),
                                                        request.getDescription(false));

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoEntityException.class)
    public ResponseEntity<?> handleValidNegativeValueException(NoEntityException exception, WebRequest request) {

        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                                                        HttpStatus.BAD_REQUEST.value(),
                                                        exception.getMessage(),
                                                        request.getDescription(false));

        return new ResponseEntity(errorDetails, HttpStatus.BAD_REQUEST);
    }

}
