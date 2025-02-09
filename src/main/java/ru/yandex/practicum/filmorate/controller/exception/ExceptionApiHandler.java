package ru.yandex.practicum.filmorate.controller.exception;


import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.exception.ExceptionsResponseGenerator.getResponse;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return getResponse(ex, log);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> constraintViolationException(ConstraintViolationException ex) {
        return getResponse(ex, log);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> nullPointerException(NullPointerException ex) {
        return getResponse(ex, log);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> sqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());
        return Map.of("message", "ConstraintViolationException");
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> emptyResultDataAccessException(EmptyResultDataAccessException ex) {
        log.error(ex.getMessage());
        return Map.of("message", "NotFoundException");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> dataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        return Map.of("message", "DataIntegrityViolationException");
    }
}
