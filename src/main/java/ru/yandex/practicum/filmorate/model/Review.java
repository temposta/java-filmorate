package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Review {

    private Long reviewId;

    @NotEmpty(message = "Содержимое отзыва не может быть пустым")
    private String content;

    @NotNull(message = "Поле оценки отзыва не может быть пустым")
    private Boolean isPositive;

    @NotNull(message = "Поле ID пользователя отзыва не может быть пустым")
    private Long userId;

    @NotNull(message = "Поле ID фильма отзыва не может быть пустым")
    private Long filmId;

    private Integer useful;
}