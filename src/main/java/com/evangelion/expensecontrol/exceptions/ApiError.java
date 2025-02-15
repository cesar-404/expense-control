package com.evangelion.expensecontrol.exceptions;

public class ApiError {
    private String message;

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
