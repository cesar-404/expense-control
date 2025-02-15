package com.evangelion.expensecontrol.exceptions;

public class PersonUderageException extends RuntimeException {
    public PersonUderageException(String message) {
        super(message);
    }
}
