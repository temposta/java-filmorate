package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 */
@Data
public class User {
    private Long id;
    @NotBlank
    @Email
    private String email;
    @Pattern(regexp = "^\\S+$")
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}
