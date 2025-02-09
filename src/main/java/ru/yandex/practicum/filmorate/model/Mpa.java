package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Mpa {
    @NotNull
    @Positive
    private Long id;
    @NotNull
    @Size(min = 1, max = 5)
    private String name;
}
