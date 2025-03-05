package ru.yandex.practicum.filmorate.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorService {

    private final DirectorStorage directorStorage;

    public List<Director> findAll() {
        return directorStorage.getAll();
    }

    public Director findById(Long id) {
        Optional<Director> director = directorStorage.read(id);
        if (director.isPresent()) {
            return director.get();
        }
        String error = String.format(ExceptionMessages.DIRECTOR_NOT_FOUND_ERROR, id);
        log.error(error);
        throw new NotFoundException(error);
    }

    public @Valid Director create(@Valid Director director) {
        return directorStorage.create(director);
    }

    public Director update(@Valid Director director) {
        return directorStorage.update(director);
    }
}