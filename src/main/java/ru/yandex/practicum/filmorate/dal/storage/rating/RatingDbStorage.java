package ru.yandex.practicum.filmorate.dal.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dal.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {

    final RatingRepository RatingRepository;

    @Override
    public List<Mpa> getAll() {
        return RatingRepository.findAll();
    }

    @Override
    public Optional<Mpa> read(Long id) {
        return RatingRepository.findById(id);
    }
}