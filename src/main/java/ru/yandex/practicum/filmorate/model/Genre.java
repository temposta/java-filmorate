package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    @Positive
    @NotNull
    private Long id;
    @Size(min = 2, max = 50)
    private String name;
}
