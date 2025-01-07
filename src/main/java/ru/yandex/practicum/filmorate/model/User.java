package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.string.NoWhiteSpace;

import java.time.LocalDate;

/**
 * User
 */
@Data
@Builder(toBuilder = true)
public class User {
    private Long id;
    @Email
    @NotEmpty
    private String email;
    @NotEmpty
    @NoWhiteSpace
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
}