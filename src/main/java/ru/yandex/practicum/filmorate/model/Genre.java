package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Genre {
    @Positive
    @NotNull
    private Long id;
    @Size(min = 2, max = 50)
    private String name;
}
