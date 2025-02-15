package com.evangelion.expensecontrol.exceptions;

public class EmptyPersonListException extends RuntimeException {
    public EmptyPersonListException(String message) {
        super(message);
    }
}
