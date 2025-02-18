package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ExceptionMessages;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dal.storage.rating.RatingStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingStorage ratingStorage;

    public List<Mpa> findAll() {
        return ratingStorage.getAll();
    }

    public Mpa findById(Long id) {
        Optional<Mpa> Rating = ratingStorage.read(id);
        if (Rating.isPresent()) {
            return Rating.get();
        }
        log.error(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, id));
        throw new NotFoundException(String.format(ExceptionMessages.RATING_NOT_FOUND_ERROR, id));
    }
}