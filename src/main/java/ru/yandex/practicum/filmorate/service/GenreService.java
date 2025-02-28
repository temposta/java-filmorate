package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.dal.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public List<Genre> findAll() {
        return genreStorage.getAll();
    }

    public Genre findById(Long id) {
        Optional<Genre> genre = genreStorage.read(id);
        if (genre.isPresent()) {
            return genre.get();
        }
        log.error(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, id));
        throw new NotFoundException(String.format(ExceptionMessages.GENRE_NOT_FOUND_ERROR, id));
    }
}