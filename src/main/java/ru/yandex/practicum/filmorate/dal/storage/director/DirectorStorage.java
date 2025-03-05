package ru.yandex.practicum.filmorate.dal.storage.director;

import jakarta.validation.Valid;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> getAll();

    Optional<Director> read(Long id);

    @Valid Director create(@Valid Director director);

    Director update(@Valid Director director);
}