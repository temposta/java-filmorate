package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.genre.GenreRepository;

import java.util.List;

@Slf4j
@Service
public class GenreService {

    GenreRepository repo;

    public GenreService(GenreRepository repo) {
        this.repo = repo;
        log.info("GenreService created");
    }

    public List<Genre> getAll() {
        log.info("Getting all genres");
        return repo.getAll();
    }

    public Genre getById(int id) {
        log.info("Getting genre by id: {}", id);
        return repo.findById(id);
    }
}
