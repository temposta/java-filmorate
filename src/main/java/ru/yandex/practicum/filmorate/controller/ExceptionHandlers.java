package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

/**
 * ExceptionHandlers.
 */
public class ExceptionHandlers {
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

    public static Map<String, String> getResponse(NullPointerException ex, Logger log) {
        Map<String, String> errors = new HashMap<>();
        errors.put("null", ex.getMessage());
        log.error(ex.getMessage());
        return errors;
    }


}
