package com.evangelion.expensecontrol.exceptions;

import java.util.Map;

public class ApiValidationError {

    private Map<String, String> errors;

    public ApiValidationError(Map<String, String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
