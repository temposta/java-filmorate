package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {

    private Long id;

    @NotEmpty
    private String content;

    private Boolean isPositive;

    @NotEmpty
    private Long userId;

    @NotEmpty
    private Long filmId;

    private Integer useful;
}