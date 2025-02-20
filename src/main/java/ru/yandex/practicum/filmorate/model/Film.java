package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.date.MinimumDate;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film
 */
@Data
@EqualsAndHashCode(of = {"name", "releaseDate"})
@Builder(toBuilder = true)
public class Film {
    private Long id;
    @NotEmpty(message = "Название не может быть пустым")
    private String name;
    @NotEmpty(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Описание не должно быть длиннее 200 символов")
    private String description;
    @MinimumDate
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Длительность не может быть меньше нуля")
    private int duration;
    private Mpa mpa;
    private Set<Genre> genres;
}