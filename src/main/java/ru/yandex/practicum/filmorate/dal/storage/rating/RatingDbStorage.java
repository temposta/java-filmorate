package ru.yandex.practicum.filmorate.dal.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dal.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {

    final RatingRepository ratingRepository;

    @Override
    public List<Mpa> getAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Optional<Mpa> read(Long id) {
        return ratingRepository.findById(id);
    }
}