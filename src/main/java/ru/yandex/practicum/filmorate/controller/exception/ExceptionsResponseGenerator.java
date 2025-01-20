package ru.yandex.practicum.filmorate.controller.exception;

import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

/**
 * ExceptionsResponseGenerator.
 */
public class ExceptionsResponseGenerator {
    public static Map<String, String> getResponse(MethodArgumentNotValidException ex, Logger log) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                    log.error("{} - {}", fieldName, errorMessage);
                });
        return errors;
    }

    public static Map<String, String> getResponse(ConstraintViolationException ex, Logger log) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(error -> {
            String property = "path error";
            String errorMessage = error.getPropertyPath().toString() + ": " + error.getMessage();
            errors.put(property, errorMessage);
            log.error("{} - {}", property, errorMessage);
        });
        return errors;
    }

    public static Map<String, String> getResponse(NullPointerException ex, Logger log) {
        Map<String, String> errors = new HashMap<>();
        errors.put("null", ex.getMessage());
        log.error(ex.getMessage());
        return errors;
    }


}
