package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.validators.MinDate;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @MinDate("1895-12-28")
    private LocalDate releaseDate;
    @Positive
    private int duration;
}
