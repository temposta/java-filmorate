package ru.yandex.practicum.filmorate.dal.storage.rating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface RatingStorage {

    List<Mpa> getAll();

    Optional<Mpa> read(Long id);
}