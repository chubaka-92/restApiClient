package ru.chubaka.springcourse.validation;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ErrorDetails {
    private Date timestemp;



    private int status;
    private String message;
    private String details;

    public ErrorDetails(Date timestemp, int status, String message, String details) {
        this.timestemp = timestemp;
        this.status = status;
        this.message = message;
        this.details = details;
    }

    public Date getTimestemp() {
        return timestemp;
    }

    public void setTimestemp(Date timestemp) {
        this.timestemp = timestemp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
