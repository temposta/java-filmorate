package ru.yandex.practicum.filmorate.dal.storage.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

public interface DirectorStorage {

    List<Director> getAll();

    Optional<Director> read(Long id);

}