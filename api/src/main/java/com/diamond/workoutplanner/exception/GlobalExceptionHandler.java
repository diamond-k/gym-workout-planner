package com.diamond.workoutplanner.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // watch all REST controllers for exceptions and handle them here.
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException exception) {
        return exception.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                // gets every failed field
                .getFieldErrors()
                .forEach(error -> errors.put(
                        // gets the name of the field that failed validation
                        error.getField(),
                        // gets the custom message for that field
                        error.getDefaultMessage()));
        return errors;
    }

    @ExceptionHandler(InvalidMuscleGroupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidMuscleGroup(InvalidMuscleGroupException exception) {
        return exception.getMessage();
    }
}