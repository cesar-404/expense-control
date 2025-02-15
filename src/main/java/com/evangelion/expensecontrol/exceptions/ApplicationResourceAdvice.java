package com.evangelion.expensecontrol.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationResourceAdvice {

    @ExceptionHandler(PersonIsNullException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlePersonIsNullException(PersonIsNullException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(PersonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handlePersonNotFoundException(PersonNotFoundException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(PersonUderageException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError handlePersonUnderageException(PersonUderageException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(EmptyPersonListException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiError handleEmptyPersonListException(EmptyPersonListException exception) {
        return new ApiError(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiValidationError handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ApiValidationError(errors);
    }
}
